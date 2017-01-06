package me.evrooij.daos;

import me.evrooij.data.Account;
import me.evrooij.util.DatabaseUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author eddy on 9-12-16.
 */
public class AccountDAOTest {

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

    private AccountDAO accountDAO;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Before
    public void setUp() throws Exception {
        accountDAO = new AccountDAO();
    }

    @After
    public void tearDown() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void create() throws Exception {
        /*
         * Simple tests to see if accounts are being created in the database
         */
        // Make sure the db is empty
        int expected_size_1 = 0;
        int actual_size_1 = accountDAO.getAmount();
        assertEquals(expected_size_1, actual_size_1);

        // Create an account
        accountDAO.create(USERNAME_1, EMAIL_1, PASSWORD_1);

        // Verify the amount has raised by 1
        int expected_size_2 = 1;
        int actual_size_2 = accountDAO.getAmount();
        assertEquals(expected_size_2, actual_size_2);


        // One more check for robustness
        // Create an account
        accountDAO.create(USERNAME_2, EMAIL_2, PASSWORD_2);

        int expected_size_3 = 2;
        int actual_size_3 = accountDAO.getAmount();
        assertEquals(expected_size_3, actual_size_3);

    }

    @Test
    public void get() throws Exception {
        /*
         * Check if we can successfully retrieve the account we just created
         */
        accountDAO.create(USERNAME_1, EMAIL_1, PASSWORD_1);
        Account actual_1 = accountDAO.get(USERNAME_1, PASSWORD_1);
        assertNotNull(actual_1);

        accountDAO.create(USERNAME_2, EMAIL_2, PASSWORD_2);
        Account actual_2 = accountDAO.get(USERNAME_1, PASSWORD_1);
        assertNotNull(actual_2);

        accountDAO.create(USERNAME_3, EMAIL_3, PASSWORD_3);
        Account actual_3 = accountDAO.get(USERNAME_1, PASSWORD_1);
        assertNotNull(actual_3);

        /*
         * Check if we won't retrieve any account at all for usernames we haven't created
         */
        Account actual_4 = accountDAO.get(USERNAME_UNUSED_1, PASSWORD_1);
        assertNull(actual_4);

        Account actual_5 = accountDAO.get(USERNAME_UNUSED_2, PASSWORD_1);
        assertNull(actual_5);

        Account actual_6 = accountDAO.get(USERNAME_UNUSED_3, PASSWORD_1);
        assertNull(actual_6);
    }

    @Test
    public void getAmount() throws Exception {
        /**
         * Gets the amount of accounts in the database
         *
         * @return integer value
         */
        // Verify it's on 0 before we start
        int expected_1 = 0;
        int actual_1 = accountDAO.getAmount();
        assertEquals(expected_1, actual_1);
        // Create an account, verify we have 1 account now
        accountDAO.create(USERNAME_1, EMAIL_1, PASSWORD_1);
        int expected_2 = 1;
        int actual_2 = accountDAO.getAmount();
        assertEquals(expected_2, actual_2);
        // Robustness checks
        accountDAO.create(USERNAME_2, EMAIL_2, PASSWORD_2);
        accountDAO.create(USERNAME_3, EMAIL_3, PASSWORD_3);
        int expected_3 = 3;
        int actual_3 = accountDAO.getAmount();
        assertEquals(expected_3, actual_3);
    }

    @Test
    public void deleteAccount() throws Exception {
        // Create an account
        Account accountToDelete = accountDAO.create(USERNAME_1, EMAIL_1, PASSWORD_1);
        // Remove it
        accountDAO.deleteAccount(accountToDelete);
        // Check if we can still get it, shouldn't work
        Account account = accountDAO.get(USERNAME_1, PASSWORD_1);
        assertNull(account);
    }

    @Test
    public void gets() throws Exception {
        /*
         * Create some accounts and verify if the amount of accounts
         * has raised in the database
         */
        int expected_1 = 0;
        int actual_1 = accountDAO.getAmount();
        assertEquals(expected_1, actual_1);

        Account account_1 = accountDAO.create(USERNAME_1, EMAIL_1, PASSWORD_1);

        int expected_2 = 1;
        int actual_2 = accountDAO.getAmount();
        assertEquals(expected_2, actual_2);

        Account account_2 = accountDAO.create(USERNAME_2, EMAIL_2, PASSWORD_2);
        Account account_3 = accountDAO.create(USERNAME_3, EMAIL_3, PASSWORD_3);

        int expected_3 = 3;
        int actual_3 = accountDAO.getAmount();
        assertEquals(expected_3, actual_3);

        /*
         * Create some accounts and verify if those are
         * actually the ones we just created
         */
        boolean passed_acc_1 = false;
        boolean passed_acc_2 = false;
        boolean passed_acc_3 = false;

        List<Account> accountList = accountDAO.getAccounts();
        for (Account a : accountList) {
            if (a.equals(account_1)) {
                passed_acc_1 = true;
            } else if (a.equals(account_2)) {
                passed_acc_2 = true;
            } else if (a.equals(account_3)) {
                passed_acc_3 = true;
            }
        }
        assertTrue(passed_acc_1);
        assertTrue(passed_acc_2);
        assertTrue(passed_acc_3);
    }

    @Test
    public void addFriend() throws Exception {
        /*
         * Make some accounts and verify they're friends with each other after
         */
        Account account_1 = accountDAO.create(USERNAME_1, EMAIL_1, PASSWORD_1);
        Account account_2 = accountDAO.create(USERNAME_2, EMAIL_2, PASSWORD_2);
        Account account_3 = accountDAO.create(USERNAME_3, EMAIL_3, PASSWORD_3);
        // Add account 2 as friend to account 1
        accountDAO.addFriend(account_1.getId(), account_2);
        // Verify they're now friends
        boolean result_1 = account_1.isFriendsWith(account_2.getId());
        assertTrue(result_1);

        // Robustness check
        account_3.addFriend(account_2);
        boolean result_2 = account_3.isFriendsWith(account_2.getId());
        assertTrue(result_2);
    }
}






