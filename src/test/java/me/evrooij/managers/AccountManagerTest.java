package me.evrooij.managers;

import me.evrooij.domain.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.*;

/**
 * @author eddy on 8-12-16.
 */
public class AccountManagerTest {


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getAccount() throws Exception {

    }

    @Test
    public void registerAccount() throws Exception {
        for (int i = 0; i < 10; i++) {
            new AccountManager().saveToDatabase(new Account("User", "Mail", "Password"));
        }
        assertEquals(1, 1);
    }

}