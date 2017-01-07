package me.evrooij.services;

import com.google.gson.Gson;
import me.evrooij.data.Feedback;
import me.evrooij.data.ResponseMessage;
import me.evrooij.managers.FeedbackManager;
import me.evrooij.util.JsonUtil;

import static me.evrooij.Config.PATH_FEEDBACK_DELETE;
import static me.evrooij.Config.PATH_FEEDBACK_NEW;
import static me.evrooij.util.JsonUtil.json;
import static spark.Spark.*;

public class FeedbackService {
    private FeedbackManager feedbackManager;

    public FeedbackService() {
        feedbackManager = new FeedbackManager();

        delete(PATH_FEEDBACK_DELETE, (request, response) -> {
            int id = Integer.valueOf(request.params(":id"));

            if (feedbackManager.deleteFeedback(id)) {
                return new ResponseMessage("Successfully deleted feedback.");
            } else {
                return new ResponseMessage("Error: feedback was not deleted.");
            }
        });

        post(PATH_FEEDBACK_NEW, (request, response) -> {
            String json = request.body();
            System.out.println(String.format("Received json2 from /bugs/new in req body: %s", json));

            Feedback feedback = new Gson().fromJson(json, Feedback.class);
            System.out.println(String.format("Retrieved feedback: %s", feedback.toString()));

            Feedback feedbackFromDb = feedbackManager.reportFeedback(feedback.getMessage(), feedback.getType(), feedback.getSender());

            if (feedbackFromDb != null) {
                System.out.println(String.format("Feedback inserted: %s", feedbackFromDb.toString()));
                response.status(400);
                return new ResponseMessage("Thank you for your feedback!");
            } else {
                return new ResponseMessage("Could not insert feedback. Please try again later.");
            }

        }, json());

        after((request, response) -> response.type("application/json"));

        exception(Exception.class, (exception, request, response) -> {
            response.status(400);
            response.type("application/json");
            response.body(JsonUtil.toJson(new ResponseMessage(exception)));
        });
    }
}
