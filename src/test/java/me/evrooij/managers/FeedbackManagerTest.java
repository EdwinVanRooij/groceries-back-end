package me.evrooij.managers;

import me.evrooij.domain.Account;
import me.evrooij.domain.Feedback;
import me.evrooij.util.DatabaseUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author eddy on 30-12-16.
 */
public class FeedbackManagerTest {

    private AccountManager accountManager;
    private FeedbackManager feedbackManager;

    private static final String USERNAME = "111111";
    private static final String EMAIL = "mail@gmail.com";
    private static final String PASS = "mypass1332";

    private Account thisAccount;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Before
    public void setUp() throws Exception {
        accountManager = new AccountManager();
        feedbackManager = new FeedbackManager();

        thisAccount = accountManager.registerAccount(USERNAME, EMAIL, PASS);
    }

    @After
    public void tearDown() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void createFeedback() throws Exception {
        /*
         * Check if bug feedback is created on valid parameters
         */
        String message_bug = "Adding products does not work";
        Feedback.Type type_bug = Feedback.Type.Bug;
        Feedback feedback_bug = feedbackManager.reportFeedback(message_bug, type_bug, thisAccount);
        // Check if item was created
        assertNotNull(feedback_bug);
        // Check if it's actually a bug feedback item
        assertEquals(Feedback.Type.Bug, feedback_bug.getType());

        /*
         * Check if suggestion feedback is created on valid parameters
         */
        String message_suggestion = "I'd love to be able to delete multiple products at once.";
        Feedback.Type type_suggestion = Feedback.Type.Suggestion;
        Feedback feedback_suggestion = feedbackManager.reportFeedback(message_suggestion, type_suggestion, thisAccount);
        // Check if item was created
        assertNotNull(feedback_suggestion);
        // Check if it's actually a suggestion feedback item
        assertEquals(Feedback.Type.Suggestion, feedback_suggestion.getType());

        /*
         * Check if feedback is not created on empty message
         */
        String message_empty = "";
        Feedback.Type type = Feedback.Type.Bug;
        Feedback feedback = feedbackManager.reportFeedback(message_empty, type, thisAccount);
        // Check if item was created
        assertNull(feedback);
    }

}