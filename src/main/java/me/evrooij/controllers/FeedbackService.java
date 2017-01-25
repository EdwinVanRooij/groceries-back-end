package me.evrooij.controllers;

import com.google.gson.Gson;
import me.evrooij.data.Feedback;
import me.evrooij.data.ResponseMessage;
import me.evrooij.managers.FeedbackManager;
import me.evrooij.util.JsonUtil;

import static me.evrooij.util.JsonUtil.json;
import static spark.Spark.*;

public class FeedbackService {
    private FeedbackManager feedbackManager;

    public FeedbackService() {
        feedbackManager = new FeedbackManager();

        get("/feedback", (request, response) -> {
            return feedbackManager.getAllFeedback();
        }, json());

        delete("/feedback/:id", (request, response) -> {
            int id = Integer.valueOf(request.params(":id"));

            if (feedbackManager.deleteFeedback(id)) {
                return new ResponseMessage("Successfully deleted feedback.");
            } else {
                return new ResponseMessage("Error: feedback was not deleted.");
            }
        }, json());

        post("/feedback/new", (request, response) -> {
            String json = request.body();
            System.out.println(String.format("Received json2 from /bugs/new in req body: %s", json));

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

        after((request, response) -> response.type("application/json"));

        exception(Exception.class, (exception, request, response) -> {
            response.status(400);
            response.type("application/json");
            response.body(JsonUtil.toJson(new ResponseMessage(exception)));
        });
    }
}
