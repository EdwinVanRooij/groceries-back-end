package me.evrooij.managers;

import me.evrooij.domain.Account;
import me.evrooij.domain.GroceryList;
import me.evrooij.domain.Product;
import me.evrooij.util.DatabaseUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author eddy on 23-12-16.
 */
public class GroceryListManagerTest {

    private static final String NAME_1 = "My List";
    private static final String NAME_2 = "This is another list";
    private static final String NAME_3 = "List";

    private static final String USERNAME = "111111";
    private static final String EMAIL = "mail@gmail.com";
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
        account = accountManager.registerAccount(USERNAME, EMAIL, PASSWORD);
    }

    @After
    public void tearDown() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void createGroceryList() throws Exception {
        /*
         * Create a list, verify existence by retrieving it
         */
        GroceryList list_1 = groceryListManager.createGroceryList(NAME_1, account);
        assertNotNull(list_1);
        // Check if getting this list is the same as
        // the just created list.
        GroceryList list_2 = groceryListManager.getList(list_1.getId());
        assertEquals(list_1, list_2);

        // Robustness checks
        GroceryList list_3 = groceryListManager.createGroceryList(NAME_2, account);
        assertNotNull(list_3);
        GroceryList list_4 = groceryListManager.createGroceryList(NAME_3, account);
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
        GroceryList list_1 = groceryListManager.createGroceryList(NAME_1, account);
        // Verify the one we get by account id is the one we just created
        assertEquals(list_1, groceryListManager.getListsByAccountId(account.getId()).get(0));

        // Create another list
        groceryListManager.createGroceryList(NAME_2, account);
        // Verify we have 2 lists now
        int expected_2 = 2;
        int actual_2 = groceryListManager.getListsByAccountId(account.getId()).size();
        assertEquals(expected_2, actual_2);
    }

    @Test
    public void getList() throws Exception {
        GroceryList expected = groceryListManager.createGroceryList(NAME_1, account);
        GroceryList actual = groceryListManager.getList(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    public void addProduct() throws Exception {
        /*
         * Verify a product is added to the list if the list exists
         */
        GroceryList list = groceryListManager.createGroceryList(NAME_1, account);
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
        GroceryList list = groceryListManager.createGroceryList(NAME_1, account);
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
        /**
         * Adds a new participant to a list
         * listId must point to an existent list
         * new participant must not be the owner
         * new participant must not already be in the list
         *
         * @param listId         id of the list to add participant to
         * @param newParticipant new participant to join the list
         * @return success if succeeded, false if not
         */
        // todo:
    }
}