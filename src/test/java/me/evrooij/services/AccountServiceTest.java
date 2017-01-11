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
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void test4_addFriend() throws Exception {
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
    public void test3_findFriend() throws Exception {
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
    public void test5_getFriends() throws Exception {
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
    public void test2_getAccount() throws Exception {
        String password = "password";
        Response response = get(
                String.format("/users/login?username=%s&password=%s", thisAccount.getUsername(), password)
        );
        String password2 = "password";
        Response response2 = get(
                String.format("/users/login?username=%s&password=%s", otherAccount.getUsername(), password2)
        );

        // Verify Account
        Account actual = new Gson().fromJson(response.body().string(), Account.class);
        assertEquals(thisAccount, actual);

        Account actual2 = new Gson().fromJson(response2.body().string(), Account.class);
        assertEquals(otherAccount, actual2);

        // Verify code
        assertEquals(HTTP_OK, response.code());
        assertEquals(HTTP_OK, response2.code());
    }

    @Test
    public void test1_registerAccount() throws Exception {
        String userName1 = "UsernameOne";
        String userName2 = "UsernameTwo";
        String email1 = "mailOne@gmail.com";
        String email2 = "mailTwo@gmail.com";
        String password = "password";

        Response response1 = post(
                "/users/register",
                "{\n" +
                        String.format("\"password\": \"%s\",\n", password) +
                        String.format("\"email\": \"%s\",\n", email1) +
                        String.format("\"username\": \"%s\"\n", userName1) +
                        "}"
        );
        Response response2 = post("/users/register", "{\n" + String.format("\"password\": \"%s\",\n", password) + String.format("\"email\": \"%s\",\n", email2) + String.format("\"username\": \"%s\"\n", userName2) + "}");

        // Verify
        Account actual1 = new Gson().fromJson(response1.body().string(), Account.class);
        Account actual2 = new Gson().fromJson(response2.body().string(), Account.class);
        assertNotNull(actual1);
        assertNotNull(actual2);

        // Sample
        assertEquals(userName1, actual1.getUsername());
        assertEquals(userName2, actual2.getUsername());

        // Verify code
        assertEquals(HTTP_OK, response1.code());
        assertEquals(HTTP_OK, response2.code());

        // Set thisAccount and create a second one
        thisAccount = actual1;
        otherAccount = actual2;
    }
}