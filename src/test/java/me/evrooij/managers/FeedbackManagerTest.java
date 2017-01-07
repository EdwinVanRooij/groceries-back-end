package me.evrooij.managers;

import me.evrooij.data.Account;
import me.evrooij.data.Feedback;
import me.evrooij.util.DatabaseUtil;
import me.evrooij.util.DummyDataGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.InvalidParameterException;

import static org.junit.Assert.*;

/**
 * @author eddy on 30-12-16.
 */
public class FeedbackManagerTest {

    private DummyDataGenerator dummyDataGenerator;
    private FeedbackManager feedbackManager;

    private Account thisAccount;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Before
    public void setUp() throws Exception {
        dummyDataGenerator = new DummyDataGenerator();
        feedbackManager = new FeedbackManager();

        thisAccount = dummyDataGenerator.generateAccount();
    }

    @After
    public void tearDown() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void reportFeedback() throws Exception {
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
        // Check if item was created
        try {
            feedbackManager.reportFeedback(message_empty, type, thisAccount);
            fail();
        } catch (InvalidParameterException e) {

        }
    }

    @Test
    public void deleteFeedback() throws Exception {
        /*
         * Create a feedback item, delete it
         */
        Feedback feedback = dummyDataGenerator.generateFeedback(thisAccount);
        boolean result = feedbackManager.deleteFeedback(feedback.getId());
        // Verify deletion
        assertTrue(result);

        /*
         * Now check if we get a NullPointerException on invalid id,
         * previous feedback should be deleted
         */
        try {
            feedbackManager.deleteFeedback(feedback.getId());
            fail();
        } catch (NullPointerException e) {
        }
    }

}