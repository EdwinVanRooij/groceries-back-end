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
}