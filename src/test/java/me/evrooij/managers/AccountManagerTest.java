package me.evrooij.managers;

import me.evrooij.data.Account;
import me.evrooij.exceptions.InvalidFriendRequestException;
import me.evrooij.exceptions.InvalidLoginCredentialsException;
import me.evrooij.util.DatabaseUtil;
import org.junit.*;

import java.security.InvalidParameterException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author eddy on 10-12-16.
 */
public class AccountManagerTest {

    /*
     * at least 6 characters, at max 30
     */
    private static final String CORRECT_USERNAME_1 = "222222";
    private static final String CORRECT_USERNAME_2 = "111111111111111111111111111111";
    private static final String CORRECT_USERNAME_3 = "Hankinson";
    private static final String INCORRECT_USERNAME_1 = "1";
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
    public void login() throws Exception {
        /*
         * Happy flow: correct login
         */
        // Test one
        accountManager.registerAccount(CORRECT_USERNAME_1, CORRECT_EMAIL_1, CORRECT_PASS_1);
        Account account = accountManager.login(CORRECT_USERNAME_1, CORRECT_PASS_1);
        assertNotNull(account);
        // Test two
        accountManager.registerAccount(CORRECT_USERNAME_2, CORRECT_EMAIL_2, CORRECT_PASS_2);
        Account account2 = accountManager.login(CORRECT_USERNAME_2, CORRECT_PASS_2);
        assertNotNull(account2);
        /*
         * Test if it doesn't return anything when we have a correct login, but account was not registerAccounted
         */
        // Note how we don't registerAccount the account first
        try {
            accountManager.login(CORRECT_USERNAME_3, CORRECT_PASS_3);
            fail();
        } catch (InvalidLoginCredentialsException e) {
        }
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
        try {
            accountManager.registerAccount(INCORRECT_USERNAME_1, CORRECT_EMAIL_1, CORRECT_PASS_1);
            fail();
        } catch (InvalidParameterException e) {
        }
        try {
            accountManager.registerAccount(INCORRECT_USERNAME_2, CORRECT_EMAIL_1, CORRECT_PASS_1);
            fail();
        } catch (InvalidParameterException e) {
        }
        try {
            accountManager.registerAccount(INCORRECT_USERNAME_3, CORRECT_EMAIL_1, CORRECT_PASS_1);
            fail();
        } catch (InvalidParameterException e) {
        }

        // Check incorrect email
        try {
            accountManager.registerAccount(CORRECT_USERNAME_1, INCORRECT_EMAIL_1, CORRECT_PASS_1);
            fail();
        } catch (InvalidParameterException e) {
        }
        try {
            accountManager.registerAccount(CORRECT_USERNAME_1, INCORRECT_EMAIL_2, CORRECT_PASS_1);
            fail();
        } catch (InvalidParameterException e) {
        }
        try {
            accountManager.registerAccount(CORRECT_USERNAME_1, INCORRECT_EMAIL_3, CORRECT_PASS_1);
            fail();
        } catch (InvalidParameterException e) {
        }

        // Check incorrect pass
        try {
            accountManager.registerAccount(CORRECT_USERNAME_2, CORRECT_EMAIL_3, INCORRECT_PASS_1);
            fail();
        } catch (InvalidParameterException e) {
        }
        try {
            accountManager.registerAccount(CORRECT_USERNAME_2, CORRECT_EMAIL_3, INCORRECT_PASS_2);
            fail();
        } catch (InvalidParameterException e) {
        }
        try {
            accountManager.registerAccount(CORRECT_USERNAME_2, CORRECT_EMAIL_3, INCORRECT_PASS_3);
            fail();
        } catch (InvalidParameterException e) {
        }

        // Now let's get batshit crazy
        try {
            accountManager.registerAccount(INCORRECT_USERNAME_1, INCORRECT_EMAIL_1, INCORRECT_PASS_1);
            fail();
        } catch (InvalidParameterException e) {
        }
        try {
            accountManager.registerAccount(INCORRECT_USERNAME_2, INCORRECT_EMAIL_2, INCORRECT_PASS_2);
            fail();
        } catch (InvalidParameterException e) {
        }
        try {
            accountManager.registerAccount(INCORRECT_USERNAME_3, INCORRECT_EMAIL_3, INCORRECT_PASS_3);
            fail();
        } catch (InvalidParameterException e) {
        }
    }

    @Test
    public void removeAccount() throws Exception {
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
        try {
            accountManager.removeAccount(CORRECT_USERNAME_1, CORRECT_PASS_1);
            fail();
        } catch (InvalidLoginCredentialsException e) {
        }
        // Robustness checks
        try {
            accountManager.removeAccount(CORRECT_USERNAME_3, CORRECT_PASS_3);
            fail();
        } catch (InvalidLoginCredentialsException e) {
        }
        try {
            accountManager.removeAccount(INCORRECT_USERNAME_2, INCORRECT_PASS_2);
            fail();
        } catch (InvalidLoginCredentialsException e) {
        }

        /*
         * Check if removing account with incorrect credentials returns false
         */
        // Create valid account
        accountManager.registerAccount(CORRECT_USERNAME_1, CORRECT_EMAIL_1, CORRECT_PASS_1);
        // Attempt to remove with incorrect credentials
        try {
            accountManager.removeAccount(CORRECT_USERNAME_2, INCORRECT_PASS_2);
            fail();
        } catch (InvalidLoginCredentialsException e) {
        }
        // Robustness checks
        accountManager.registerAccount(CORRECT_USERNAME_2, CORRECT_EMAIL_2, CORRECT_PASS_2);
        try {
            accountManager.removeAccount(CORRECT_USERNAME_2, INCORRECT_PASS_2);
            fail();
        } catch (InvalidLoginCredentialsException e) {
        }
        accountManager.registerAccount(CORRECT_USERNAME_3, CORRECT_EMAIL_3, CORRECT_PASS_3);
        try {
            accountManager.removeAccount(INCORRECT_USERNAME_1, CORRECT_PASS_2);
            fail();
        } catch (InvalidLoginCredentialsException e) {
        }
    }

    @Test
    public void searchFriends() throws Exception {
        // Create some variables first
        String username_searcher = "searcher";
        String username_1 = "UsernameOne";
        String username_2 = "UsernameTwo";
        String username_3 = "UsernameThree";
        String username_4 = "OneTwoThree";
        String email_searcher = "searcher@search.com";
        String email_1 = "mailOne@mail.com";
        String email_2 = "mailTwo@mail.com";
        String email_3 = "mailThree@mail.com";
        String email_4 = "OneTwoThree@mail.com";
        String password = "passwordOne";
        // Create three accounts
        Account searcher = accountManager.registerAccount(username_searcher, email_searcher, password);
        Account account_1 = accountManager.registerAccount(username_1, email_1, password);
        Account account_2 = accountManager.registerAccount(username_2, email_2, password);
        // Below accounts are named for reference purposes
        Account account_3 = accountManager.registerAccount(username_3, email_3, password);
        //noinspection unused
        Account account_4 = accountManager.registerAccount(username_4, email_4, password);
        /*
         * Check if equal usernames are found, case insensitively
         */
        // This should match one account, account_1, check if they're equal
        List<Account> accountList_1 = accountManager.searchFriends(searcher.getId(), "USERnameone");
        assertEquals(account_1, accountList_1.get(0));
        /*
         * Check if equal emails are found, case insensitively
         */
        // This should match one account, account_2, check if they're equal
        List<Account> accountList_2 = accountManager.searchFriends(searcher.getId(), "mailTWO@mail.com");
        assertEquals(account_2, accountList_2.get(0));
        /*
         * Check if partial usernames are found, case insensitively
         */
        // This should match the equal and partial username, account_2 & account_4
        List<Account> accountList_3 = accountManager.searchFriends(searcher.getId(), "two");
        int expectedSize_1 = 2;
        int actualSize_1 = accountList_3.size();
        assertEquals(expectedSize_1, actualSize_1);
        /*
         * Check if partial emails are found, case insensitively
         */
        // This should match the equal and partial email, account 3 & account 4
        List<Account> accountList_4 = accountManager.searchFriends(searcher.getId(), "three@mail");
        int expectedSize_2 = 2;
        int actualSize_2 = accountList_4.size();
        assertEquals(expectedSize_2, actualSize_2);
        /*
         * Check if not matching accounts are not found, case insensitively
         */
        int expectedSize_3 = 0;
        List<Account> accountList_5 = accountManager.searchFriends(searcher.getId(), "four");
        List<Account> accountList_6 = accountManager.searchFriends(searcher.getId(), "four@mail");
        assertEquals(expectedSize_3, accountList_5.size());
        assertEquals(expectedSize_3, accountList_6.size());
        /*
         * Check if searcher is excluded
         */
        int expectedSize_4 = 0;
        List<Account> accountList_7 = accountManager.searchFriends(searcher.getId(), "search");
        List<Account> accountList_8 = accountManager.searchFriends(searcher.getId(), "searcher");
        assertEquals(expectedSize_4, accountList_7.size());
        assertEquals(expectedSize_4, accountList_8.size());
        /*
         * Check if it doesn't match on already friends
         */
        int expectedSize_5 = 0;
        accountManager.addFriend(searcher.getId(), account_1);
        List<Account> accountList_9 = accountManager.searchFriends(searcher.getId(), account_1.getUsername());
        assertEquals(expectedSize_5, accountList_9.size());
        /*
         * Robustness checks
         */
        int expectedSize_6 = 0;
        accountManager.addFriend(searcher.getId(), account_2);
        List<Account> accountList_10 = accountManager.searchFriends(searcher.getId(), account_2.getEmail());
        assertEquals(expectedSize_6, accountList_10.size());

        int expectedSize_7 = 0;
        accountManager.addFriend(searcher.getId(), account_3);
        List<Account> accountList_11 = accountManager.searchFriends(searcher.getId(), account_3.getUsername());
        assertEquals(expectedSize_7, accountList_11.size());
    }

    @Test
    public void addFriend() throws Exception {
        /*
         * Verify that friend is added to account if they're not friends yet
         */
        Account account_1 = accountManager.registerAccount(CORRECT_USERNAME_1, CORRECT_EMAIL_1, CORRECT_PASS_1);
        Account account_2 = accountManager.registerAccount(CORRECT_USERNAME_2, CORRECT_EMAIL_2, CORRECT_PASS_2);
        boolean result_1 = accountManager.addFriend(account_1.getId(), account_2);
        assertTrue(result_1);
        // Robustness check
        Account account_3 = accountManager.registerAccount(CORRECT_USERNAME_3, CORRECT_EMAIL_3, CORRECT_PASS_3);
        boolean result_2 = accountManager.addFriend(account_1.getId(), account_3);
        assertTrue(result_2);
        /*
         * Verify that accounts are added as friends reversely as well
         */
        // Account 1 added two accounts as friends
        int expectedSize_1 = 2;
        int actualSize_1 = accountManager.getFriends(account_1.getId()).size();
        assertEquals(expectedSize_1, actualSize_1);
        // Account 2 and 3 were both added by one account, account 1
        int expectedSize_2 = 1;
        int actualSize_2 = accountManager.getFriends(account_2.getId()).size();
        assertEquals(expectedSize_2, actualSize_2);

        int expectedSize_3 = 1;
        int actualSize_3 = accountManager.getFriends(account_2.getId()).size();
        assertEquals(expectedSize_3, actualSize_3);

        /*
         * Verify that friend is not added to an account who they're friends with already
         */
        // Account_2 was added as friend to account_1, so check if we can't add account_2 to account_1 again
        try {
            accountManager.addFriend(account_1.getId(), account_2);
            fail();
        } catch (InvalidFriendRequestException e) {
        }
    }

    @Test
    public void getFriends() throws Exception {
        /*
         * Verify the friends are being returned on valid accountId
         */
        // Create some accounts
        Account account_1 = accountManager.registerAccount(CORRECT_USERNAME_1, CORRECT_EMAIL_1, CORRECT_PASS_1);
        Account friend_1 = accountManager.registerAccount(CORRECT_USERNAME_2, CORRECT_EMAIL_2, CORRECT_PASS_2);
        Account friend_2 = accountManager.registerAccount(CORRECT_USERNAME_3, CORRECT_EMAIL_3, CORRECT_PASS_3);
        // Add a friend to first account
        accountManager.addFriend(account_1.getId(), friend_1);
        // Verify added friend is now in the friend list of account_1
        assertEquals(friend_1, account_1.getFriends().get(0));

        // Robustness check, verify if there are 2 friends in account_1's friend list after adding friend_2
        accountManager.addFriend(account_1.getId(), friend_2);
        int expectedSize_1 = 2;
        int actualSize_1 = accountManager.getFriends(account_1.getId()).size();
        assertEquals(expectedSize_1, actualSize_1);

        /*
         * Verify there's nothing returned on invalid accountId
         */
        int invalidAccountId = account_1.getId() - 1;
        assertNull(accountManager.getFriends(invalidAccountId));
    }

    @Test
    public void getFriendsOfEachOther() throws Exception {
        /*
         * I'm still suspicious about getting friends of an account
         * who's friends with the said account. Robustness checks here.
         */
        // Check if we can add each other
        Account account_1 = accountManager.registerAccount(CORRECT_USERNAME_1, CORRECT_EMAIL_1, CORRECT_PASS_1);
        Account account_2 = accountManager.registerAccount(CORRECT_USERNAME_2, CORRECT_EMAIL_2, CORRECT_PASS_1);
        Account account_3 = accountManager.registerAccount(CORRECT_USERNAME_3, CORRECT_EMAIL_3, CORRECT_PASS_1);

        // Add account 2 as friends to account 1
        boolean result = accountManager.addFriend(account_1.getId(), account_2);
        assertTrue(result);

        // Add account 3 as friends to account 1
        boolean result_2 = accountManager.addFriend(account_1.getId(), account_3);
        assertTrue(result_2);

        // Check if account 1 now has two friends
        int expectedSize = 2;
        int actualSize = accountManager.getFriends(account_1.getId()).size();
        assertEquals(expectedSize, actualSize);

        // Check if account 2 now has one friend, account 1
        assertEquals(account_1, accountManager.getFriends(account_2.getId()).get(0));

        // Check if account 3 now has one friend, account 1
        assertEquals(account_1, accountManager.getFriends(account_3.getId()).get(0));
    }
}

