package me.evrooij.managers;

import me.evrooij.domain.Account;
import me.evrooij.util.DatabaseUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author eddy on 10-12-16.
 */
public class AccountManagerTest {

    /*
     * at least 6 characters, at max 30
     */
    private static final String CORRECT_USERNAME_1 = "111111";
    private static final String CORRECT_USERNAME_2 = "111111111111111111111111111111";
    private static final String CORRECT_USERNAME_3 = "Hankinson";
    private static final String INCORRECT_USERNAME_1 = "11111";
    private static final String INCORRECT_USERNAME_2 = "1111111111111111111111111111111";
    private static final String INCORRECT_USERNAME_3 = "hank@hankinson.com";
    private static final String CORRECT_EMAIL_1 = "mail@gmail.com";
    private static final String CORRECT_EMAIL_2 = "somestring@student.fontys.me";
    private static final String CORRECT_EMAIL_3 = "info@evrooij.me";
    private static final String INCORRECT_EMAIL_1 = "@mail.com";
    private static final String INCORRECT_EMAIL_2 = "info@.com";
    private static final String INCORRECT_EMAIL_3 = "info@me.";
    /*
     * at least 8 characters, at max 100
     */
    private static final String CORRECT_PASS_1 = "11111111";
    private static final String CORRECT_PASS_2 = "mypass1332";
    private static final String CORRECT_PASS_3 = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
    private static final String INCORRECT_PASS_1 = "1111111";
    private static final String INCORRECT_PASS_2 = "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
    private static final String INCORRECT_PASS_3 = "r11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";

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
         * Happy flow: correct getAccount
         */
        // Test one
        accountManager.registerAccount(CORRECT_USERNAME_1, CORRECT_EMAIL_1, CORRECT_PASS_1);
        Account account = accountManager.getAccount(CORRECT_USERNAME_1, CORRECT_PASS_1);
        assertNotNull(account);
        // Test two
        accountManager.registerAccount(CORRECT_USERNAME_2, CORRECT_EMAIL_2, CORRECT_PASS_2);
        Account account2 = accountManager.getAccount(CORRECT_USERNAME_2, CORRECT_PASS_2);
        assertNotNull(account2);
        /*
         * Test if it doesn't return anything when we have a correct getAccount, but account was not registerAccounted
         */
        // Note how we don't registerAccount the account first
        Account accountFail = accountManager.getAccount(CORRECT_USERNAME_3, CORRECT_PASS_3);
        assertNull(accountFail);
    }

    @Test
    public void registerAccount() throws Exception {
        Account account = accountManager.registerAccount(CORRECT_USERNAME_1, CORRECT_EMAIL_1, CORRECT_PASS_1);
        assertNotNull(account);
        Account account2 = accountManager.registerAccount(CORRECT_USERNAME_2, CORRECT_EMAIL_2, CORRECT_PASS_2);
        assertNotNull(account2);
        Account account3 = accountManager.registerAccount(CORRECT_USERNAME_3, CORRECT_EMAIL_3, CORRECT_PASS_3);
        assertNotNull(account3);

        /*
         * Check whether the registration fails at any incorrect parameter
         */

        // Check incorrect username
        Account incorrectAccountUsername1 = accountManager.registerAccount(INCORRECT_USERNAME_1, CORRECT_EMAIL_1, CORRECT_PASS_1);
        assertNull(incorrectAccountUsername1);
        Account incorrectAccountUsername2 = accountManager.registerAccount(INCORRECT_USERNAME_2, CORRECT_EMAIL_1, CORRECT_PASS_1);
        assertNull(incorrectAccountUsername2);
        Account incorrectAccountUsername3 = accountManager.registerAccount(INCORRECT_USERNAME_3, CORRECT_EMAIL_1, CORRECT_PASS_1);
        assertNull(incorrectAccountUsername3);

        // Check incorrect email
        Account incorrectAccountEmail1 = accountManager.registerAccount(CORRECT_USERNAME_1, INCORRECT_EMAIL_1, CORRECT_PASS_1);
        assertNull(incorrectAccountEmail1);
        Account incorrectAccountEmail2 = accountManager.registerAccount(CORRECT_USERNAME_1, INCORRECT_EMAIL_2, CORRECT_PASS_1);
        assertNull(incorrectAccountEmail2);
        Account incorrectAccountEmail3 = accountManager.registerAccount(CORRECT_USERNAME_1, INCORRECT_EMAIL_3, CORRECT_PASS_1);
        assertNull(incorrectAccountEmail3);

        // Check incorrect pass
        Account incorrectAccountPass = accountManager.registerAccount(CORRECT_USERNAME_2, CORRECT_EMAIL_3, INCORRECT_PASS_1);
        assertNull(incorrectAccountPass);
        Account incorrectAccountPass2 = accountManager.registerAccount(CORRECT_USERNAME_2, CORRECT_EMAIL_3, INCORRECT_PASS_2);
        assertNull(incorrectAccountPass2);
        Account incorrectAccountPass3 = accountManager.registerAccount(CORRECT_USERNAME_2, CORRECT_EMAIL_3, INCORRECT_PASS_3);
        assertNull(incorrectAccountPass3);

        // Now let's get batshit crazy
        Account incorrectAccountEverything = accountManager.registerAccount(INCORRECT_USERNAME_1, INCORRECT_EMAIL_1, INCORRECT_PASS_1);
        assertNull(incorrectAccountEverything);
        Account incorrectAccountEverything2 = accountManager.registerAccount(INCORRECT_USERNAME_2, INCORRECT_EMAIL_2, INCORRECT_PASS_2);
        assertNull(incorrectAccountEverything2);
        Account incorrectAccountEverything3 = accountManager.registerAccount(INCORRECT_USERNAME_3, INCORRECT_EMAIL_3, INCORRECT_PASS_3);
        assertNull(incorrectAccountEverything3);
    }

    @Test
    public void removeAccount() throws Exception {
        /**
         * Removes an account from the database if:
         * - the username/password combination is correct
         * - the account exists
         *
         * @param username
         * @param password
         * @return boolean value indicating the exit status of removal
         */
        /*
         * Happy flow
         */
        // Create valid account
        accountManager.registerAccount(CORRECT_USERNAME_1, CORRECT_EMAIL_1, CORRECT_PASS_1);
        // Remove account correctly
        boolean result_1 = accountManager.removeAccount(CORRECT_USERNAME_1, CORRECT_PASS_1);
        assertTrue(result_1);

        /*
         * Check if removing non-existent accounts returns false
         */
        // Try to remove previous account again, which should be deleted now
        boolean result_2 = accountManager.removeAccount(CORRECT_USERNAME_1, CORRECT_PASS_1);
        assertFalse(result_2);
        // Robustness checks
        boolean result_3 = accountManager.removeAccount(CORRECT_USERNAME_3, CORRECT_PASS_3);
        assertFalse(result_3);
        boolean result_4 = accountManager.removeAccount(INCORRECT_USERNAME_2, INCORRECT_PASS_2);
        assertFalse(result_4);

        /*
         * Check if removing account with incorrect credentials returns false
         */
        // Create valid account
        accountManager.registerAccount(CORRECT_USERNAME_1, CORRECT_EMAIL_1, CORRECT_PASS_1);
        // Attempt to remove with incorrect credentials
        boolean result_5 = accountManager.removeAccount(CORRECT_USERNAME_2, INCORRECT_PASS_2);
        assertFalse(result_5);
        // Robustness checks
        accountManager.registerAccount(CORRECT_USERNAME_2, CORRECT_EMAIL_2, CORRECT_PASS_2);
        boolean result_6 = accountManager.removeAccount(CORRECT_USERNAME_2, INCORRECT_PASS_2);
        assertFalse(result_6);
        accountManager.registerAccount(CORRECT_USERNAME_3, CORRECT_EMAIL_3, CORRECT_PASS_3);
        boolean result_7 = accountManager.removeAccount(INCORRECT_USERNAME_1, CORRECT_PASS_2);
        assertFalse(result_7);
    }

    @Test
    public void searchFriends() throws Exception {
        /**
         * Searches for an account that somewhat matches the search query word
         *
         * @param searchQuery the user entered search query, matches on:
         *                    (all of these are case insensitive)
         *                    - query equals username
         *                    - query equals email
         *                    - query partially equals username
         *                    - query partially equals email
         * @return an list with accounts that match the search query
         */
        // Create some variables first
        String username_1 = "UsernameOne";
        String username_2 = "UsernameTwo";
        String username_3 = "UsernameThree";
        String username_4 = "OneTwoThree";
        String email_1 = "mailOne@mail.com";
        String email_2 = "mailTwo@mail.com";
        String email_3 = "mailThree@mail.com";
        String email_4 = "OneTwoThree@mail.com";
        String password = "passwordOne";
        // Create three accounts
        Account account_1 = accountManager.registerAccount(username_1, email_1, password);
        Account account_2 = accountManager.registerAccount(username_2, email_2, password);
        // Below accounts are named for reference purposes
        Account account_3 = accountManager.registerAccount(username_3, email_3, password);
        Account account_4 = accountManager.registerAccount(username_4, email_4, password);
        /*
         * Check if equal usernames are found, case insensitively
         */
        // This should match one account, account_1, check if they're equal
        List<Account> accountList_1 = accountManager.searchFriends("USERnameone");
        assertEquals(account_1, accountList_1.get(0));
        /*
         * Check if equal emails are found, case insensitively
         */
        // This should match one account, account_2, check if they're equal
        List<Account> accountList_2 = accountManager.searchFriends("mailTWO@mail.com");
        assertEquals(account_2, accountList_2.get(0));
        /*
         * Check if partial usernames are found, case insensitively
         */
        // This should match the equal and partial username, account_2 & account_4
        List<Account> accountList_3 = accountManager.searchFriends("two");
        int expectedSize_1 = 2;
        int actualSize_1 = accountList_3.size();
        assertEquals(expectedSize_1, actualSize_1);
        /*
         * Check if partial emails are found, case insensitively
         */
        // This should match the equal and partial email, account 3 & account 4
        List<Account> accountList_4 = accountManager.searchFriends("three@mail");
        int expectedSize_2 = 2;
        int actualSize_2 = accountList_4.size();
        assertEquals(expectedSize_2, actualSize_2);
        /*
         * Check if not matching accounts are not found, case insensitively
         */
        int expectedSize = 0;
        List<Account> accountList_5 = accountManager.searchFriends("four");
        List<Account> accountList_6 = accountManager.searchFriends("four@mail");
        assertEquals(expectedSize, accountList_5.size());
        assertEquals(expectedSize, accountList_6.size());
    }
}








