package me.evrooij.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.evrooij.data.Account;
import me.evrooij.data.ResponseMessage;
import me.evrooij.util.DatabaseUtil;
import me.evrooij.util.DummyDataGenerator;
import okhttp3.Response;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static me.evrooij.NetworkUtil.*;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.*;

/**
 * @author eddy on 1-1-17.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountServiceTest {

    private static Account thisAccount;
    private static Account otherAccount;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new DatabaseUtil().clean();
        DummyDataGenerator dummyDataGenerator = new DummyDataGenerator();
        thisAccount = dummyDataGenerator.generateAccount();
        otherAccount = dummyDataGenerator.generateAccount();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void test1_addFriend() throws Exception {
        Response response = post(
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
        assertEquals(HTTP_OK, response.code());
    }

    @Test
    public void test2_findFriend() throws Exception {
        // Using '@' as search query, should return one account, since
        // we added another account in the test before this one. @ matches with any email.
        String query = "@";

        Response response = get(
                String.format("/accounts/%s/friends/find?query=%s", thisAccount.getId(), query)
        );

        // Verify message
        Type listType = new TypeToken<ArrayList<Account>>() {
        }.getType();
        List<Account> list = new Gson().fromJson(response.body().string(), listType);
        assertEquals(1, list.size());

        // Verify code
        assertEquals(HTTP_OK, response.code());
    }

    @Test
    public void test3_getFriends() throws Exception {
        Response response = get(
                String.format("/accounts/%s/friends", thisAccount.getId())
        );

        // Verify friend
        Type listType = new TypeToken<ArrayList<Account>>() {
        }.getType();
        List<Account> list = new Gson().fromJson(response.body().string(), listType);
        assertEquals(otherAccount, list.get(0));
        assertEquals(1, list.size());

        // Verify code
        assertEquals(HTTP_OK, response.code());
    }

    @Test
    public void getAccount() throws Exception {
        Response response = get(
                String.format("/users/login?username=%s&password=%s", thisAccount.getUsername(), thisAccount.getPassword())
        );

        // Verify Account
        Account actual = new Gson().fromJson(response.body().string(), Account.class);
        assertEquals(thisAccount, actual);

        // Verify code
        assertEquals(HTTP_OK, response.code());
    }

    @Test
    public void registerAccount() throws Exception {
        String extraString = "salt";
        Response response = post(
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
        assertEquals(HTTP_OK, response.code());
    }
}