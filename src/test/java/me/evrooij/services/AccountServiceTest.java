package me.evrooij.services;

import com.google.gson.Gson;
import me.evrooij.data.Account;
import me.evrooij.managers.AccountManager;
import me.evrooij.data.ResponseMessage;
import me.evrooij.util.DatabaseUtil;
import me.evrooij.util.NetworkUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author eddy on 1-1-17.
 */
public class AccountServiceTest {

    private static final String USERNAME_1 = "111111";
    private static final String USERNAME_2 = "222222";
    private static final String EMAIL_1 = "mail1@gmail.com";
    private static final String EMAIL_2 = "mail2@gmail.com";
    private static final String PASSWORD = "thisi4sapassword";

    private AccountManager accountManager;
    private Account thisAccount;
    private Account otherAccount;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Before
    public void setUp() throws Exception {
        accountManager = new AccountManager();
        thisAccount = accountManager.registerAccount(USERNAME_1, EMAIL_1, PASSWORD);
        otherAccount = accountManager.registerAccount(USERNAME_2, EMAIL_2, PASSWORD);
    }

    @After
    public void tearDown() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void testAddFriend() throws Exception {
//        Adds a new friend to both users, the one initiating the friend request and the one being added.
        String body = String.format("{\n\"id\": %s,\n\"username\": \"%s\",\n\"email\": \"%s\"\n}", String.valueOf(otherAccount.getId()), otherAccount.getUsername(), otherAccount.getEmail());

        String response = NetworkUtil.post(String.format("/accounts/%s/friends/add", thisAccount.getId()), body);

        ResponseMessage expected = new ResponseMessage("Successfully added friend.");
        ResponseMessage actual = new Gson().fromJson(response, ResponseMessage.class);
        assertEquals(expected, actual);

    }
}