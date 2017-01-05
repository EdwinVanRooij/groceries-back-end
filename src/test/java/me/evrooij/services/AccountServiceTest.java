package me.evrooij.services;

import com.google.gson.Gson;
import me.evrooij.data.Account;
import me.evrooij.data.ResponseMessage;
import me.evrooij.util.DatabaseUtil;
import me.evrooij.util.DummyDataGenerator;
import me.evrooij.util.NetworkUtil;
import okhttp3.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

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
    public void testFindFriend() throws Exception {
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
    }

    @Test
    public void testAddFriend() throws Exception {
        Response response = NetworkUtil.post(
//                /accounts/2159/friends/add
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
    }
}