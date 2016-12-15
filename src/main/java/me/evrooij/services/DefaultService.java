package me.evrooij.services;

import me.evrooij.responses.ResponseMessage;
import me.evrooij.util.JsonUtil;
import spark.Request;
import spark.Response;

public class DefaultService {
    /**
     * Called before processing a request
     *
     * @param request  the received request from outside
     * @param response the response to send back
     */
    protected void beforeRouteHandle(Request request, Response response) {
        // Handle before
    }

    /**
     * Called after processing a request
     *
     * @param request  the received request from outside
     * @param response the response to send back
     */
    protected void afterRouteHandle(Request request, Response response) {
        response.type("application/json");
    }

    /**
     * Handles all exceptions
     *
     * @param exception the caused exception
     * @param request   the invalid request from outside
     * @param response  the response to send back
     */
    protected void handleException(Exception exception, Request request, Response response) {
        response.status(400);
        response.type("application/json");
        response.body(JsonUtil.toJson(new ResponseMessage(exception)));
    }
}
