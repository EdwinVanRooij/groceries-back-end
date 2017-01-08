package me.evrooij.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.evrooij.TestConfig;
import me.evrooij.data.Account;
import me.evrooij.data.Feedback;
import me.evrooij.data.Product;
import me.evrooij.data.ResponseMessage;
import me.evrooij.managers.FeedbackManager;
import me.evrooij.util.DatabaseUtil;
import me.evrooij.util.DummyDataGenerator;
import me.evrooij.util.NetworkUtil;
import okhttp3.Response;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * @author eddy on 7-1-17.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FeedbackServiceTest {

    private static Account thisAccount;

    private static Feedback createdFeedback;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new DatabaseUtil().clean();
        DummyDataGenerator dummyDataGenerator = new DummyDataGenerator();
        thisAccount = dummyDataGenerator.generateAccount();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void test2_getAllFeedback() throws Exception {
        Response response = NetworkUtil.get(
                "/feedback"
        );

        // Verify message
        Type listType = new TypeToken<ArrayList<Feedback>>() {
        }.getType();
        List<Feedback> list = new Gson().fromJson(response.body().string(), listType);
        assertNotNull(list);
        assertEquals(1, list.size());

        // For later deleteFeedback test
        createdFeedback = list.get(0);

        // Verify code
        assertEquals(HTTP_OK, response.code());
    }

    @Test
    public void test3_deleteFeedback() throws Exception {
        Response response = NetworkUtil.delete(
                String.format("/feedback/%s", createdFeedback.getId())
        );

        // Verify message
        ResponseMessage expectedMessage = new ResponseMessage("Successfully deleted feedback.");
        ResponseMessage actualMessage = new Gson().fromJson(response.body().string(), ResponseMessage.class);
        assertEquals(expectedMessage, actualMessage);

        // Verify code
        assertEquals(HTTP_OK, response.code());
    }

    @Test
    public void test1_newFeedback() throws Exception {
        String message = "I can't open my second list!";
        Feedback.Type type = Feedback.Type.Bug;
        int typeInt = type.ordinal();

        Response response = NetworkUtil.post(
                "/feedback/new",
                "{\n" +
                        String.format("\"message\": \"%s\",\n", message) +
                        String.format("\"type\": %s,\n", typeInt) +
                        "\"sender\": {\n" +
                        String.format("    \"id\": %s,\n", thisAccount.getId()) +
                        String.format("    \"username\": \"%s\",\n", thisAccount.getUsername()) +
                        String.format("    \"email\": \"%s\"\n", thisAccount.getEmail()) +
                        "    }\n" +
                        "}"
        );

        // Verify
        ResponseMessage expectedMessage = new ResponseMessage("Thank you for your feedback!");
        ResponseMessage actualMessage = new Gson().fromJson(response.body().string(), ResponseMessage.class);
        assertEquals(expectedMessage, actualMessage);

        // Verify code
        assertEquals(HTTP_OK, response.code());
    }
}
