package me.evrooij.managers;

import me.evrooij.domain.Account;
import me.evrooij.util.DatabaseUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author eddy on 10-12-16.
 */
public class AccountManagerTest {
    private static final String USERNAME_1 = "ThisIsAUsername34";
    private static final String USERNAME_2 = "AnotherUsername";
    private static final String USERNAME_3 = "ThisIsMyUsername!!";
    private static final String USERNAME_UNUSED_1 = "unused_username1";
    private static final String USERNAME_UNUSED_2 = "unused_username2";
    private static final String USERNAME_UNUSED_3 = "unused_username3";

    private static final String EMAIL_1 = "email@mail.com";
    private static final String EMAIL_2 = "mymail@gmail.com";
    private static final String EMAIL_3 = "email2@mail.com";

    private static final String PASSWORD_1 = "thisis!dapassword";
    private static final String PASSWORD_2 = "thisi4sapassword";
    private static final String PASSWORD_3 = "this2isapassword";

    private AccountManager accountManager;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Before
    public void setUp() throws Exception {
        accountManager = new AccountManager();
    }

    @After
    public void tearDown() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void getAccount() throws Exception {
        /*
         * Register some accounts and verify that we can retrieve them
         */

        // Register
        accountManager.registerAccount(USERNAME_1, EMAIL_1, PASSWORD_1);

        // Get the actual account
        Account actual_1 = accountManager.getAccount(USERNAME_1, PASSWORD_1);
        assertNotNull(actual_1);

        // Make sure we can't get an uncreated account
        Account actual_2 = accountManager.getAccount(USERNAME_2, PASSWORD_2);
        assertNull(actual_2);

        Account actual_3 = accountManager.getAccount(USERNAME_3, PASSWORD_3);
        assertNull(actual_3);
    }

    @Test
    public void registerAccount() throws Exception {
        /*
         * Create accounts and verify their equality
         * Can't use assertEquals here because we don't know the generated ID for each individual account
         */

        // Register
        accountManager.registerAccount(USERNAME_1, EMAIL_1, PASSWORD_1);

        // Get the actual account
        Account actual_1 = accountManager.getAccount(USERNAME_1, PASSWORD_1);

        // Verify it's existence
        assertNotNull(actual_1);

        // Now some robustness checks, verify creation of 2 more accounts
        accountManager.registerAccount(USERNAME_2, EMAIL_2, PASSWORD_2);
        Account actual_2 = accountManager.getAccount(USERNAME_1, PASSWORD_1);
        assertNotNull(actual_2);

        accountManager.registerAccount(USERNAME_3, EMAIL_3, PASSWORD_3);
        Account actual_3 = accountManager.getAccount(USERNAME_1, PASSWORD_1);
        assertNotNull(actual_3);
    }

    @Test
    public void removeAccount() throws Exception {
        /*
         * Remove some existing accounts from the database
         */

        // Register
        accountManager.registerAccount(USERNAME_1, EMAIL_1, PASSWORD_1);
        boolean actual_1 = accountManager.removeAccount(USERNAME_1, PASSWORD_1);
        // Verify that this removal was successful
        assertTrue(actual_1);

        // Try to remove a non-existent account
        boolean actual_2 = accountManager.removeAccount(USERNAME_2, PASSWORD_2);
        assertFalse(actual_2);
    }
}