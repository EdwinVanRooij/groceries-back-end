package me.evrooij;

import spark.Request;
import spark.Response;

import static me.evrooij.JsonUtil.json;
import static spark.Spark.*;

public class UserController {

    public UserController(final UserService userService) {

        get("/users", (request, response) -> userService.getAllUsers(), json());

        get("/users", (req, res) -> userService.getAllUsers(), json());

        get("/users/:id", (req, res) -> {
            String id = req.params(":id");
            User user = userService.getUser(id);
            if (user != null) {
                return user;
            }
            res.status(400);
            return new ResponseError("No user with id '%s' found", id);
        }, json());

        post("/users", (req, res) -> userService.createUser(
                req.queryParams("name"),
                req.queryParams("email")
        ), json());

        put("/users/:id", (req, res) -> userService.updateUser(
                req.params(":id"),
                req.queryParams("name"),
                req.queryParams("email")
        ), json());

        before(this::beforeRouteHandle);
        after(this::afterRouteHandle);
        exception(Exception.class, this::handleException);
    }

    /**
     * Called before processing a request
     *
     * @param request  the received request from outside
     * @param response the response to send back
     */
    private void beforeRouteHandle(Request request, Response response) {
        // Handle before
    }

    /**
     * Called after processing a request
     *
     * @param request  the received request from outside
     * @param response the response to send back
     */
    private void afterRouteHandle(Request request, Response response) {
        response.type("application/json");
    }

    /**
     * Handles all exceptions
     *
     * @param exception the caused exception
     * @param request   the invalid request from outside
     * @param response  the response to send back
     */
    private void handleException(Exception exception, Request request, Response response) {
        response.status(400);
        response.body(JsonUtil.toJson(new ResponseError(exception)));
    }
}
