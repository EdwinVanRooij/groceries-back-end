package me.evrooij.services;

import me.evrooij.data.Account;
import me.evrooij.managers.AccountManager;
import me.evrooij.util.DatabaseUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.junit.Assert.*;

/**
 * @author eddy on 5-1-17.
 */
public class GroceryListServiceTest {

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



}