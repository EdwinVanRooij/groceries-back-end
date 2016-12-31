package me.evrooij.services;

import com.google.gson.Gson;
import me.evrooij.data.Feedback;
import me.evrooij.managers.FeedbackManager;
import me.evrooij.responses.ResponseMessage;

import static me.evrooij.util.JsonUtil.json;
import static spark.Spark.*;

public class FeedbackService extends DefaultService {
    private FeedbackManager feedbackManager;

    public FeedbackService() {
        feedbackManager = new FeedbackManager();

        post("/feedback/new", (request, response) -> {
            String json = request.body();
            System.out.println(String.format("Received json from /bugs/new in req body: %s", json));

            Feedback feedback = new Gson().fromJson(json, Feedback.class);
            System.out.println(String.format("Retrieved feedback: %s", feedback.toString()));

            Feedback feedbackFromDb = feedbackManager.reportFeedback(feedback.getMessage(), feedback.getType(), feedback.getSender());

            if (feedbackFromDb != null) {
                System.out.println(String.format("Feedback inserted: %s", feedbackFromDb.toString()));
                return new ResponseMessage("Thank you for your feedback!");
            } else {
                return new ResponseMessage("Could not insert feedback. Please try again later.");
            }

        }, json());

        before(this::beforeRouteHandle);
        after(this::afterRouteHandle);
        exception(Exception.class, this::handleException);
    }
}
