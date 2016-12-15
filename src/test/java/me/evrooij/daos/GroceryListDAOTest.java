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

    private GroceryListDAO groceryListDAO;
    private Account account;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Before
    public void setUp() throws Exception {
        // Create an account to work with
        String username = "ThisIsAUsername34";
        String email = "email@mail.com";
        String password = "thisis!dapassword";
        account = new AccountManager().registerAccount(username, email, password);

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
    public void getLists() throws Exception {
        /**
         * Returns all GroceryLists an account is in
         *
         * @param id unique identifier for the account
         * @return
         */

    }

}