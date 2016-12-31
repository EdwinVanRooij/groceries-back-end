package me.evrooij.managers;

import me.evrooij.daos.FeedbackDAO;
import me.evrooij.data.Account;
import me.evrooij.data.Feedback;

public class FeedbackManager {

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
     */
    public Feedback reportFeedback(String message, Feedback.Type type, Account sender) {
        if (message.isEmpty()) {
            // Message is empty, return null
            return null;
        }
        return feedbackDAO.create(message, type, sender);
    }
}