package me.evrooij.services;

import me.evrooij.data.Account;
import me.evrooij.managers.AccountManager;
import me.evrooij.util.DatabaseUtil;
import me.evrooij.util.DummyDataGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author eddy on 5-1-17.
 */
public class GroceryListServiceTest {

    private DummyDataGenerator dummyDataGenerator;
    private Account thisAccount;
    private Account otherAccount;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Before
    public void setUp() throws Exception {
        dummyDataGenerator = new DummyDataGenerator();
        thisAccount = dummyDataGenerator.generateAccount();
        otherAccount = dummyDataGenerator.generateAccount();
    }

    @After
    public void tearDown() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void test() throws Exception {

    }


}