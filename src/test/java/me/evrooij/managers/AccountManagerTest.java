package me.evrooij.managers;

import me.evrooij.domain.Account;
import org.junit.After;
import org.junit.Before;
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

    @Before
    public void setUp() throws Exception {
        accountManager = new AccountManager();

    }

    @Test
    public void getAccount() throws Exception {
        /**
         * Returns an account
         *
         * @param username
         * @param password
         * @return
         */
        accountManager.registerAccount(USERNAME_1, EMAIL_1, PASSWORD_1);

        accountManager.registerAccount(USERNAME_1, EMAIL_1, PASSWORD_1);

        accountManager.registerAccount(USERNAME_1, EMAIL_1, PASSWORD_1);

    }

    @Test
    public void registerAccount() throws Exception {
        /**
         * Registers an account
         *
         * @param username
         * @param email
         * @param password
         * @return
         */

    }

    @Test
    public void removeAccount() throws Exception {
        /**
         * Removes an account from the database if the username/password combination is correct
         *
         * @param username
         * @param password
         * @return boolean value indicating the exit status of removal
         */

    }
}