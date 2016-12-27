package me.evrooij.managers;

import com.fasterxml.jackson.core.sym.Name1;
import me.evrooij.domain.Account;
import me.evrooij.domain.GroceryList;
import me.evrooij.domain.Product;
import me.evrooij.util.DatabaseUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author eddy on 23-12-16.
 */
public class GroceryListManagerTest {

    private static final String NAME_1 = "My List";
    private static final String NAME_2 = "This is another list";
    private static final String NAME_3 = "List";

    private static final String USERNAME_1 = "111111";
    private static final String USERNAME_2 = "222222";
    private static final String USERNAME_3 = "333333";
    private static final String EMAIL_1 = "mail1@gmail.com";
    private static final String EMAIL_2 = "mail2@gmail.com";
    private static final String EMAIL_3 = "mail3@gmail.com";
    private static final String PASSWORD = "thisi4sapassword";

    private Account account;
    private GroceryListManager groceryListManager;
    private AccountManager accountManager;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Before
    public void setUp() throws Exception {
        groceryListManager = new GroceryListManager();
        accountManager = new AccountManager();
        account = accountManager.registerAccount(USERNAME_1, EMAIL_1, PASSWORD);
    }

    @After
    public void tearDown() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void createGroceryListWithInitialParticipants() throws Exception {
        /**
         * Creates a GroceryList
         *
         * @param name         name of the list
         * @param owner        initial creator of the list
         * @param participants list of accounts participating in this list
         *                     if left null or length 0, don't use it
         * @return
         */
        /*
         * Check if no participants are added on 0 length participants addition
         */
        List<Account> participants_1 = new ArrayList<>();
        GroceryList list_1 = groceryListManager.createGroceryList(NAME_1, account, participants_1);
        int expectedSize_1 = 0;
        assertEquals(expectedSize_1, list_1.getAmountOfParticipants());
        /*
         * Check if all participants are added on initial participants addition
         */
        participants_1.add(accountManager.registerAccount(USERNAME_2, EMAIL_2, PASSWORD));
        participants_1.add(accountManager.registerAccount(USERNAME_3, EMAIL_3, PASSWORD));
        GroceryList list_2 = groceryListManager.createGroceryList(NAME_2, account, participants_1);
        int expectedSize_2 = 2;
        assertEquals(expectedSize_2, list_2.getAmountOfParticipants());
    }

    @Test
    public void createGroceryList() throws Exception {
        /*
         * Create a list, verify existence by retrieving it
         */
        GroceryList list_1 = groceryListManager.createGroceryList(NAME_1, account, null);
        assertNotNull(list_1);
        // Check if getting this list is the same as
        // the just created list.
        GroceryList list_2 = groceryListManager.getList(list_1.getId());
        assertEquals(list_1, list_2);

        // Robustness checks
        GroceryList list_3 = groceryListManager.createGroceryList(NAME_2, account, null);
        assertNotNull(list_3);
        GroceryList list_4 = groceryListManager.createGroceryList(NAME_3, account, null);
        assertNotNull(list_4);
    }

    @Test
    public void getListsByAccountId() throws Exception {
        /*
         * Get current lists by account id, should be 0
         */
        int expected = 0;
        int actual = groceryListManager.getListsByAccountId(account.getId()).size();
        assertEquals(expected, actual);

        // Create a list
        GroceryList list_1 = groceryListManager.createGroceryList(NAME_1, account, null);
        // Verify the one we get by account id is the one we just created
        assertEquals(list_1, groceryListManager.getListsByAccountId(account.getId()).get(0));

        // Create another list
        groceryListManager.createGroceryList(NAME_2, account, null);
        // Verify we have 2 lists now
        int expected_2 = 2;
        int actual_2 = groceryListManager.getListsByAccountId(account.getId()).size();
        assertEquals(expected_2, actual_2);
    }

    @Test
    public void getList() throws Exception {
        GroceryList expected = groceryListManager.createGroceryList(NAME_1, account, null);
        GroceryList actual = groceryListManager.getList(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    public void addProduct() throws Exception {
        /*
         * Verify a product is added to the list if the list exists
         */
        GroceryList list = groceryListManager.createGroceryList(NAME_1, account, null);
        String name = "Apples";
        int amount = 3;
        String comment = "The red ones";
        String owner = "Foo";
        Product product = new Product(name, amount, comment, owner);
        Product productFromList = groceryListManager.addProduct(list.getId(), product);
        assertNotNull(productFromList);

        /*
         * Verify a product is not added to the list if the list doesn't exist
         */
        // Make sure we get use a list id that doesn't exist
        GroceryList nonExistentList;
        int index = 1000;
        do {
            index++;
            nonExistentList = groceryListManager.getList(index);
        } while (nonExistentList != null);
        // List is null when it exits this loop, get the id (index) used to keep the list null

        Product productFromList_2 = groceryListManager.addProduct(index, product);
        assertNull(productFromList_2);
    }

    @Test
    public void deleteProduct() throws Exception {
        /**
         * Deletes a product by id from the list by list id
         *
         * @param listId    id of the list to delete product from
         * @param productId id of product to delete
         * @return true if operation succeeded, false if it didn't
         */
        /*
         * Verify an existent product is deleted after deleteProduct
         */
        GroceryList list = groceryListManager.createGroceryList(NAME_1, account, null);
        Product product = new Product("Apples", 3, "The red ones", "Foo");
        Product productFromList = groceryListManager.addProduct(list.getId(), product);
        boolean result = groceryListManager.deleteProduct(list.getId(), productFromList.getId());
        assertTrue(result);
        /*
         * Verify the result is false if the product to delete doesn't exist
         */
        boolean result_2 = groceryListManager.deleteProduct(list.getId(), productFromList.getId());
        assertFalse(result_2);
    }

    @Test
    public void addParticipant() throws Exception {
        /*
         * Check if participant was added to list with good listId
         */
        GroceryList list = groceryListManager.createGroceryList(NAME_1, account, null);
        Account newParticipant = accountManager.registerAccount(USERNAME_2, EMAIL_2, PASSWORD);
        boolean result_1 = groceryListManager.addParticipant(list.getId(), newParticipant);
        // Verify if this worked at all
        assertTrue(result_1);
        // Get list from manager just to be sure
        GroceryList listFromManager = groceryListManager.getList(list.getId());
        assertEquals(newParticipant, listFromManager.getParticipants().get(0));

        /*
         * Check if owner is not being added to the new list
         */
        boolean result_2 = groceryListManager.addParticipant(list.getId(), account);
        assertFalse(result_2);

        /*
         * Check if we can't add newParticipant again
         */
        boolean result_3 = groceryListManager.addParticipant(list.getId(), newParticipant);
        assertFalse(result_3);
    }

    @Test
    public void updateProduct() throws Exception {
        // Create some valid objects
        GroceryList list = groceryListManager.createGroceryList(NAME_1, account, null);
        String name = "apples";
        int amount = 10;
        String comment = "red ones";
        String owner = "foobar";
        list.addProduct(name, amount, comment, owner);
        Product product = list.getProduct(name, owner, comment);
        /*
         * Check if product isn't updated on invalid listId
         */
        assertFalse(groceryListManager.updateProduct(
                list.getId() - 1,
                product.getId(),
                product));
        /*
         * Check if product isn't updated on invalid productId
         */
        assertFalse(groceryListManager.updateProduct(list.getId(), product.getId() - 1, product));
        /*
         * Check if product is updated on valid listId and productId
         */
        // Declare some new fields
        String newName = "pears";
        int newAmount = 11;
        String newComment = "purple ones";

        // Update and verify fields
        product.setName(newName);
        product.setAmount(newAmount);
        product.setComment(newComment);

        groceryListManager.updateProduct(list.getId(), product.getId(), product);
        Product productFromManager = groceryListManager.getList(list.getId()).getProduct(product.getId());

        String actualName = productFromManager.getName();
        assertEquals(newName, actualName);

        int actualAmount = productFromManager.getAmount();
        assertEquals(newAmount, actualAmount);

        String actualComment = productFromManager.getComment();
        assertEquals(newComment, actualComment);
    }
}






