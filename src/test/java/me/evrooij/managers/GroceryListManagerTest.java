package me.evrooij.managers;

import me.evrooij.domain.Account;
import me.evrooij.domain.GroceryList;
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
}