package me.evrooij.services;

import com.google.gson.Gson;
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
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author eddy on 7-1-17.
 */
public class FeedbackServiceTest {

    private DummyDataGenerator dummyDataGenerator;

    private Account thisAccount;

    private FeedbackManager feedbackManager;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Before
    public void setUp() throws Exception {
        dummyDataGenerator = new DummyDataGenerator();


        thisAccount = dummyDataGenerator.generateAccount();
    }

    @After
    public void tearDown() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void deleteFeedback() throws Exception {
        new Thread(() -> {
            try {
                Feedback feedback = dummyDataGenerator.generateFeedback(thisAccount);

                Response response = NetworkUtil.delete(
                        String.format("/feedback/%s", feedback.getId())
                );

                // Verify message
                ResponseMessage expectedMessage = new ResponseMessage("Successfully deleted feedback.");
                ResponseMessage actualMessage = new Gson().fromJson(response.body().string(), ResponseMessage.class);
                assertEquals(expectedMessage, actualMessage);

                // Verify code
                int expectedCode = 200;
                int actualCode = response.code();
                assertEquals(expectedCode, actualCode);
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }).start();

        Thread.sleep(TestConfig.SLEEP_TIME);
    }

    @Test
    public void newFeedback() throws Exception {
        new Thread(() -> {
            try {
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
                int expectedCode = 200;
                int actualCode = response.code();
                assertEquals(expectedCode, actualCode);
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }).start();

        Thread.sleep(TestConfig.SLEEP_TIME);
    }
}
