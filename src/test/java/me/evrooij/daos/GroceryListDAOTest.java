package me.evrooij.daos;

import me.evrooij.domain.Account;
import me.evrooij.domain.GroceryList;
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
        new DatabaseUtil().clean();

        // Create an account to work with
        String username = "ThisIsAUsername34";
        String email = "email@mail.com";
        String password = "thisis!dapassword";
        account = new AccountManager().registerAccount(username, email, password);
        anotherAccount = new AccountManager().registerAccount(username, email, password);

        groceryListDAO = new GroceryListDAO();
    }

    @After
    public void tearDown() throws Exception {
//        new DatabaseUtil().clean();
    }

    @Test
    public void create() throws Exception {
        /*
         * Create a list
         */
        GroceryList groceryList_1 = groceryListDAO.create(NAME_1, account);
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
        GroceryList groceryList_2 = groceryListDAO.create(NAME_2, account);
        assertNotNull(groceryList_2);

        GroceryList groceryList_3 = groceryListDAO.create(NAME_3, account);
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
        groceryListDAO.create(NAME_1, account);
        /*
         * Verify that the user is in one list now
         */
        int expected_2 = 1;
        int actual_2 = groceryListDAO.getAmountOfListsForUser(account.getId());
        assertEquals(expected_2, actual_2);
        /*
         * Now check if we als get more lists if we were added to a list by someone else
         */
//        groceryListDAO.create(NAME_2, anotherAccount);
        // todo: check if we get more lists when added to another list by someone else
        /*
         * Robustness checks
         */
        groceryListDAO.create(NAME_3, account);
        groceryListDAO.create(NAME_4, account);
        groceryListDAO.create(NAME_5, account);
        groceryListDAO.create(NAME_6, account);
        int expected_3 = 5;
        int actual_3 = groceryListDAO.getAmountOfListsForUser(account.getId());
        assertEquals(expected_3, actual_3);
    }

    @Test
    public void getLists() throws Exception {
        /*
         * Create a list
         */
        GroceryList list_1 = groceryListDAO.create(NAME_1, account);
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
        groceryListDAO.create(NAME_2, account);
        GroceryList list_3 = groceryListDAO.create(NAME_3, account);
        groceryListDAO.create(NAME_4, account);
        groceryListDAO.create(NAME_5, account);
        groceryListDAO.create(NAME_6, account);
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
}