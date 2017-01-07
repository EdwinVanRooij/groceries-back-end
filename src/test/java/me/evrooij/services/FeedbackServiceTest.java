package me.evrooij.services;

import com.google.gson.Gson;
import me.evrooij.TestConfig;
import me.evrooij.data.Account;
import me.evrooij.data.Feedback;
import me.evrooij.data.ResponseMessage;
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

/**
 * @author eddy on 7-1-17.
 */
public class FeedbackServiceTest {
    private DummyDataGenerator dummyDataGenerator;

    private Account thisAccount;

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
                                String.format("    \"email\": \"%s\",\n", thisAccount.getEmail()) +
                                String.format("    \"password\": \"%s\"\n", thisAccount.getPassword()) +
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
            }
        }).start();

        Thread.sleep(TestConfig.SLEEP_TIME);
    }
}