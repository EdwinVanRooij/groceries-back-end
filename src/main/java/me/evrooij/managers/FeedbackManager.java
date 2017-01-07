package me.evrooij.managers;

import me.evrooij.daos.FeedbackDAO;
import me.evrooij.data.Account;
import me.evrooij.data.Feedback;

import java.security.InvalidParameterException;

public class FeedbackManager {

    private String EXCEPTION_FEEDBACK_DOES_NOT_EXIST = "Feedback with id %s does not exist.";

    private FeedbackDAO feedbackDAO;

    public FeedbackManager() {
        feedbackDAO = new FeedbackDAO();
    }

    /**
     * Creates feedback
     *
     * @param message message of the feedback
     *                must not be empty
     * @param type    feedback type, bug or suggestion
     * @param sender  the account object of the user who sent this feedback
     * @return feedback if valid parameters, null if not
     * @throws InvalidParameterException on invalid parameter(s)
     */
    public Feedback reportFeedback(String message, Feedback.Type type, Account sender) throws InvalidParameterException {
        if (message.isEmpty()) {
            // Message is empty, return null
            throw new InvalidParameterException("Message should not be empty.");
        }
        return feedbackDAO.create(message, type, sender);
    }

    /**
     * Deletes feedback
     *
     * @param id id of the feedback item
     * @return true if feedback was deleted successfully, false if not
     */
    public boolean deleteFeedback(int id) {
        Feedback feedback = feedbackDAO.get(id);
        if (feedback == null) {
            throw new NullPointerException(String.format(EXCEPTION_FEEDBACK_DOES_NOT_EXIST, id));
        }
        feedbackDAO.delete(feedback);
        return true;
    }
}
