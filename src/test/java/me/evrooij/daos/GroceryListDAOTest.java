package me.evrooij.daos;

import me.evrooij.data.Account;
import me.evrooij.data.GroceryList;
import me.evrooij.data.Product;
import me.evrooij.managers.AccountManager;
import me.evrooij.util.DatabaseUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author eddy on 15-12-16.
 */
public class GroceryListDAOTest {

    private static final String NAME_1 = "My List";
    private static final String NAME_2 = "This is another list";
    private static final String NAME_3 = "List";
    private static final String NAME_4 = "List100";
    private static final String NAME_5 = "List200";
    private static final String NAME_6 = "Listyyy";

    private GroceryListDAO groceryListDAO;
    private Account account;
    private Account anotherAccount;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Before
    public void setUp() throws Exception {
        // Create an account to work with
        String username = "ThisIsAUsername34";
        String username2 = "ThisIsAUsername342";
        String email = "email@mail.com";
        String email2 = "email2@mail.com";
        String password = "thisis!dapassword";
        account = new AccountManager().registerAccount(username, email, password);
        anotherAccount = new AccountManager().registerAccount(username2, email2, password);

        groceryListDAO = new GroceryListDAO();
    }

    @After
    public void tearDown() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void create() throws Exception {
        /*
         * Create a list
         */
        GroceryList groceryList_1 = groceryListDAO.create(NAME_1, account, null);
        /*
         * Verify it's existence
         */
        assertNotNull(groceryList_1);
        /*
         * Some sanity checks
         */
        assertNotNull(groceryList_1.getId());
        assertNotNull(groceryList_1.getName());
        assertNotNull(groceryList_1.getOwner());

        /*
         * Create two more lists for robustness check
         */
        GroceryList groceryList_2 = groceryListDAO.create(NAME_2, account, null);
        assertNotNull(groceryList_2);

        GroceryList groceryList_3 = groceryListDAO.create(NAME_3, account, null);
        assertNotNull(groceryList_3);
    }

    @Test
    public void getAmountOfListsForUser() throws Exception {
        /*
         * Verify the user is in no lists to start off with
         */
        int expected_1 = 0;
        int actual_1 = groceryListDAO.getAmountOfListsForUser(account.getId());
        assertEquals(expected_1, actual_1);
        /*
         * Create a grocery lists
         */
        groceryListDAO.create(NAME_1, account, null);
        /*
         * Verify that the user is in one list now
         */
        int expected_2 = 1;
        int actual_2 = groceryListDAO.getAmountOfListsForUser(account.getId());
        assertEquals(expected_2, actual_2);
        /*
         * Robustness checks
         */
        groceryListDAO.create(NAME_3, account, null);
        groceryListDAO.create(NAME_4, account, null);
        groceryListDAO.create(NAME_5, account, null);
        groceryListDAO.create(NAME_6, account, null);
        int expected_3 = 5;
        int actual_3 = groceryListDAO.getAmountOfListsForUser(account.getId());
        assertEquals(expected_3, actual_3);
    }

    @Test
    public void getLists() throws Exception {
        /*
         * Create a list
         */
        GroceryList list_1 = groceryListDAO.create(NAME_1, account, null);
        /*
         * Verify that the user is the owner of this list
         */
        assertEquals(account, list_1.getOwner());
        /*
         * We only added one list, so the first list should be the one we created
         */
        GroceryList actual = groceryListDAO.getLists(account.getId()).get(0);
        assertEquals(list_1, actual);
        /*
         * Now add 5 more lists
         * Check if list_3 is in one of the lists the user is in
         */
        groceryListDAO.create(NAME_2, account, null);
        GroceryList list_3 = groceryListDAO.create(NAME_3, account, null);
        groceryListDAO.create(NAME_4, account, null);
        groceryListDAO.create(NAME_5, account, null);
        groceryListDAO.create(NAME_6, account, null);
        // Create boolean to verify later on
        boolean foundList_3 = false;
        for (GroceryList list : groceryListDAO.getLists(account.getId())) {
            if (list.equals(list_3)) {
                foundList_3 = true;
            }
        }
        /*
         * Verify we found list 3
         */
        assertTrue(foundList_3);
    }

    @Test
    public void getListsOwnerAndParticipant() throws Exception {
//     Returns all GroceryLists an account is in, this includes where the account is owner and where it's a participant
        /*
         * Check if account is in 3 lists when it created one and was added to 2 other ones
         */
        // Account is in no lists now
        assertEquals(0, groceryListDAO.getAmountOfListsForUser(account.getId()));
        GroceryList list_1 = groceryListDAO.create(NAME_1, account, null);
        // Account is in one list now after creating one
        assertEquals(1, groceryListDAO.getAmountOfListsForUser(account.getId()));

        // Create a list as anotherAccount, add account as participant
        GroceryList list_2 = groceryListDAO.create(NAME_2, anotherAccount, null);
        list_2.addParticipant(account);
        // Account is in two lists now, after being added to one
        assertEquals(2, groceryListDAO.getAmountOfListsForUser(account.getId()));
        // Meanwhile another account is in one, after just creating one
        assertEquals(1, groceryListDAO.getAmountOfListsForUser(anotherAccount.getId()));

        // Robustness checks, create a list for both accounts
        GroceryList list_3 = groceryListDAO.create(NAME_4, account, null);
        assertEquals(3, groceryListDAO.getAmountOfListsForUser(account.getId()));
        // Second list another account created
        GroceryList list_4 = groceryListDAO.create(NAME_3, anotherAccount, null);
        assertEquals(2, groceryListDAO.getAmountOfListsForUser(anotherAccount.getId()));

        // Add another account to list 3, check if he's now in 3 lists
        list_3.addParticipant(anotherAccount);
        assertEquals(3, groceryListDAO.getAmountOfListsForUser(anotherAccount.getId()));
    }

    @Test
    public void getList() throws Exception {
        /*
         * Verify if the lists are equal when we retrieve the created one
         * from the database again.
         */
        GroceryList list_1 = groceryListDAO.create(NAME_1, account, null);
        GroceryList list_2 = groceryListDAO.getList(list_1.getId());
        assertEquals(list_1, list_2);
        /*
         * Robustness checks
         */
        GroceryList list_3 = groceryListDAO.create(NAME_2, account, null);
        GroceryList list_4 = groceryListDAO.getList(list_3.getId());
        assertEquals(list_3, list_4);

        GroceryList list_5 = groceryListDAO.create(NAME_3, account, null);
        GroceryList list_6 = groceryListDAO.getList(list_5.getId());
        assertEquals(list_5, list_6);

        GroceryList list_7 = groceryListDAO.create(NAME_4, account, null);
        GroceryList list_8 = groceryListDAO.getList(list_7.getId());
        assertEquals(list_7, list_8);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void addProduct() throws Exception {
        /*
         * Verify the created product equals the added product
         */
        // Create a list
        GroceryList list = groceryListDAO.create(NAME_1, account, null);
        // Declare some product info
        String name_1 = "Apples1";
        String name_2 = "Apples2";
        String name_3 = "Apples3";
        int amount_1 = 10;
        int amount_2 = 12;
        int amount_3 = 13;
        String comment_1 = "Ther red ones1";
        String comment_2 = "Ther red ones2";
        String comment_3 = "Ther red ones3";
        String owner_1 = "Foo1";
        String owner_2 = "Foo2";
        String owner_3 = "Foo3";
        // Some final products
        Product product_1 = new Product(name_1, amount_1, comment_1, owner_1);
        Product productFromList_1 = groceryListDAO.addProduct(list.getId(), product_1);
        // Verify equality
        assertEquals(product_1, productFromList_1);

        // Robustness checks
        Product product_2 = new Product(name_2, amount_2, comment_2, owner_2);
        Product productFromList_2 = groceryListDAO.addProduct(list.getId(), product_2);
        assertEquals(product_2, productFromList_2);

        Product product_3 = new Product(name_3, amount_3, comment_3, owner_3);
        Product productFromList_3 = groceryListDAO.addProduct(list.getId(), product_3);
        assertEquals(product_3, productFromList_3);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void deleteProduct() throws Exception {
        /*
         * Verify the created product was deleted
         */
        // Create a list
        GroceryList list = groceryListDAO.create(NAME_1, account, null);
        // Declare some product info
        String name_1 = "Apples1";
        String name_2 = "Apples2";
        String name_3 = "Apples3";
        int amount_1 = 10;
        int amount_2 = 12;
        int amount_3 = 13;
        String comment_1 = "Ther red ones1";
        String comment_2 = "Ther red ones2";
        String comment_3 = "Ther red ones3";
        String owner_1 = "Foo1";
        String owner_2 = "Foo2";
        String owner_3 = "Foo3";

        // Verify deletion
        Product product_1 = new Product(name_1, amount_1, comment_1, owner_1);
        Product productFromList_1 = groceryListDAO.addProduct(list.getId(), product_1);
        groceryListDAO.deleteProduct(list.getId(), productFromList_1.getId());
        GroceryList list_2 = groceryListDAO.getList(list.getId());
        Product deletedProduct = list_2.getProduct(productFromList_1.getId());
        assertNull(deletedProduct);

        // Robustness checks
        Product product_2 = new Product(name_2, amount_2, comment_2, owner_2);
        Product product_3 = new Product(name_3, amount_3, comment_3, owner_3);
        Product productFromList_2 = groceryListDAO.addProduct(list.getId(), product_2);
        Product productFromList_3 = groceryListDAO.addProduct(list.getId(), product_3);
        groceryListDAO.deleteProduct(list.getId(), productFromList_2.getId());
        groceryListDAO.deleteProduct(list.getId(), productFromList_3.getId());

        GroceryList list_3 = groceryListDAO.getList(list.getId());

        Product deletedProduct_2 = list_3.getProduct(productFromList_2.getId());
        Product deletedProduct_3 = list_3.getProduct(productFromList_3.getId());
        assertNull(deletedProduct_2);
        assertNull(deletedProduct_3);
    }

    @Test
    public void update() throws Exception {
        // Declare some variables
        String productName = "name";
        String productComment = "comment";
        int productAmount = 10;
        String productOwner = "owner";
        /*
         * Check if an added product is still added in db after update
         */
        // Add product to a list
        GroceryList list_1 = groceryListDAO.create(NAME_1, account, null);
        list_1.addProduct(productName, productAmount, productComment, productOwner);
        groceryListDAO.update(list_1);
        // Get the added product
        Product addedProduct = list_1.getProduct(productName, productOwner, productComment);

        // Get the same stuff from dao
        GroceryList listFromDao = groceryListDAO.getList(list_1.getId());
        Product addedProductFromDao = listFromDao.getProduct(productName, productOwner, productComment);

        // Verify the products are there & equal
        assertEquals(addedProduct, addedProductFromDao);
    }
}



