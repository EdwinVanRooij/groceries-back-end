package me.evrooij.services;

import com.google.gson.Gson;
import me.evrooij.TestConfig;
import me.evrooij.data.Account;
import me.evrooij.data.ResponseMessage;
import me.evrooij.managers.AccountManager;
import me.evrooij.util.DatabaseUtil;
import me.evrooij.util.DummyDataGenerator;
import me.evrooij.util.NetworkUtil;
import okhttp3.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author eddy on 1-1-17.
 */
public class AccountServiceTest {

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
    public void findFriend() throws Exception {
        new Thread(() -> {
            try {
                // Using '@' as search query, should return one account, since
                // we added another account in the setup. @ matches with any email.
                String query = "@";

                Response response = NetworkUtil.get(
                        String.format("/accounts/%s/friends/find?query=%s", thisAccount.getId(), query)
                );

                // Verify message
                @SuppressWarnings("unchecked") List<Account> list = new Gson().fromJson(response.body().string(), List.class);
                assertEquals(1, list.size());

                // Verify code
                int expectedCode = 200;
                int actualCode = response.code();
                assertEquals(expectedCode, actualCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(TestConfig.SLEEP_TIME);
    }

    @Test
    public void addFriend() throws Exception {
        new Thread(() -> {
            try {
                Response response = NetworkUtil.post(
                        String.format("/accounts/%s/friends/add", thisAccount.getId()),
                        "{\n" +
                                String.format("\"id\": %s,\n", otherAccount.getId()) +
                                String.format("\"username\": \"%s\",\n", otherAccount.getUsername()) +
                                String.format("\"email\": \"%s\"\n", otherAccount.getEmail()) +
                                "}"
                );

                // Verify message
                ResponseMessage expectedMessage = new ResponseMessage("Successfully added friend.");
                ResponseMessage actualMessage = new Gson().fromJson(response.body().string(), ResponseMessage.class);
                assertEquals(expectedMessage, actualMessage);

                // Verify code
                int expectedCode = 200;
                int actualCode = response.code();
                assertEquals(expectedCode, actualCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(TestConfig.SLEEP_TIME);
    }

    @Test
    public void getAccount() throws Exception {
        new Thread(() -> {
            try {
                Response response = NetworkUtil.get(
                        String.format("/users/login?username=%s&password=%s", thisAccount.getUsername(), thisAccount.getPassword())
                );

                // Verify Account
                Account actual = new Gson().fromJson(response.body().string(), Account.class);
                assertEquals(thisAccount, actual);

                // Verify code
                int expectedCode = 200;
                int actualCode = response.code();
                assertEquals(expectedCode, actualCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(TestConfig.SLEEP_TIME);
    }

    @Test
    public void getFriends() throws Exception {
        AccountManager accountManager = new AccountManager();
        accountManager.addFriend(thisAccount.getId(), otherAccount);

        new Thread(() -> {
            try {
                Response response = NetworkUtil.get(
                        String.format("/accounts/%s/friends", thisAccount.getId())
                );

                // Verify friend
                @SuppressWarnings("unchecked") List<Account> list = new Gson().fromJson(response.body().string(), List.class);
                Account actual = list.get(0);
                assertEquals(thisAccount, actual);

                // Verify code
                int expectedCode = 200;
                int actualCode = response.code();
                assertEquals(expectedCode, actualCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(TestConfig.SLEEP_TIME);
    }

    @Test
    public void registerAccount() throws Exception {
        String extraString = "salt";
        new Thread(() -> {
            try {
                Response response = NetworkUtil.post(
                        "/users/register",
                        "{\n" +
                                String.format("\"password\": \"%s%s\",\n", extraString, thisAccount.getPassword()) +
                                String.format("\"email\": \"%s%s\",\n", extraString, thisAccount.getEmail()) +
                                String.format("\"username\": \"%s%s\"\n", extraString, thisAccount.getUsername()) +
                                "}"
                );

                // Verify
                Account actual = new Gson().fromJson(response.body().string(), Account.class);
                assertNotNull(actual);

                // Sample
                assertEquals(extraString + thisAccount.getUsername(), actual.getUsername());

                // Verify code
                int expectedCode = 200;
                int actualCode = response.code();
                assertEquals(expectedCode, actualCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(TestConfig.SLEEP_TIME);
    }
}