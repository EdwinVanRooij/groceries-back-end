package me.evrooij.daos;

import me.evrooij.domain.Account;
import org.junit.Before;
import org.junit.Test;

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

    @Before
    public void setUp() throws Exception {
        accountDAO = new AccountDAO();
    }

    @Test
    public void register() throws Exception {
        /*
         * Simple tests to see if accounts are being created in the database
         */
        // Make sure the db is empty
        int expected_size_1 = 0;
        int actual_size_1 = accountDAO.getAmountOfAccounts();
        assertEquals(expected_size_1, actual_size_1);

        // Create an account
        accountDAO.register(USERNAME_1, EMAIL_1, PASSWORD_1);

        // Verify the amount has raised by 1
        int expected_size_2 = 1;
        int actual_size_2 = accountDAO.getAmountOfAccounts();
        assertEquals(expected_size_2, actual_size_2);


        // One more check for robustness
        // Create an account
        accountDAO.register(USERNAME_2, EMAIL_2, PASSWORD_2);

        int expected_size_3 = 2;
        int actual_size_3 = accountDAO.getAmountOfAccounts();
        assertEquals(expected_size_3, actual_size_3);

    }

    @Test
    public void getAccount() throws Exception {
        /*
         * Check if we can successfully retrieve the account we just created
         */
        accountDAO.register(USERNAME_1, EMAIL_1, PASSWORD_1);
        Account actual_1 = accountDAO.getAccount(USERNAME_1, PASSWORD_1);
        assertNotNull(actual_1);

        accountDAO.register(USERNAME_2, EMAIL_2, PASSWORD_2);
        Account actual_2 = accountDAO.getAccount(USERNAME_1, PASSWORD_1);
        assertNotNull(actual_2);

        accountDAO.register(USERNAME_3, EMAIL_3, PASSWORD_3);
        Account actual_3 = accountDAO.getAccount(USERNAME_1, PASSWORD_1);
        assertNotNull(actual_3);

        /*
         * Check if we won't retrieve any account at all for usernames we haven't created
         */
        Account actual_4 = accountDAO.getAccount(USERNAME_UNUSED_1, PASSWORD_1);
        assertNull(actual_4);

        Account actual_5 = accountDAO.getAccount(USERNAME_UNUSED_2, PASSWORD_1);
        assertNull(actual_5);

        Account actual_6 = accountDAO.getAccount(USERNAME_UNUSED_3, PASSWORD_1);
        assertNull(actual_6);
    }

}