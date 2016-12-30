package me.evrooij.managers;

import com.sun.istack.internal.Nullable;
import me.evrooij.daos.FeedbackDAO;
import me.evrooij.daos.GroceryListDAO;
import me.evrooij.domain.Account;
import me.evrooij.domain.Feedback;
import me.evrooij.domain.GroceryList;
import me.evrooij.domain.Product;

import java.util.List;

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
