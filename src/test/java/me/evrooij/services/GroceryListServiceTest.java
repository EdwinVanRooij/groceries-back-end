package me.evrooij.services;

import com.google.gson.Gson;
import me.evrooij.data.Account;
import me.evrooij.data.GroceryList;
import me.evrooij.data.ResponseMessage;
import me.evrooij.util.DatabaseUtil;
import me.evrooij.util.DummyDataGenerator;
import me.evrooij.util.NetworkUtil;
import okhttp3.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author eddy on 5-1-17.
 */
public class GroceryListServiceTest {

    private DummyDataGenerator dummyDataGenerator;
    private Account thisAccount;
    private Account otherAccount;
    private GroceryList list;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Before
    public void setUp() throws Exception {
        dummyDataGenerator = new DummyDataGenerator();

        thisAccount = dummyDataGenerator.generateAccount();
        otherAccount = dummyDataGenerator.generateAccount();

        list = dummyDataGenerator.generateList(thisAccount);
    }

    @After
    public void tearDown() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void test() throws Exception {
        Response response = NetworkUtil.post(
                String.format("/lists/%s/participants/new", list.getId()),
                "{\n" +
                        String.format("\"id\": %s,\n", otherAccount.getId()) +
                        String.format("\"username\": \"%s\",\n", otherAccount.getUsername()) +
                        String.format("\"email\": \"%s\"\n", otherAccount.getEmail()) +
                        "}"
        );

        // Verify message
        ResponseMessage expectedMessage = new ResponseMessage("Successfully added new participant.");
        ResponseMessage actualMessage = new Gson().fromJson(response.body().string(), ResponseMessage.class);
        assertEquals(expectedMessage, actualMessage);

        // Verify code
        int expectedCode = 200;
        int actualCode = response.code();
        assertEquals(expectedCode, actualCode);
    }


}