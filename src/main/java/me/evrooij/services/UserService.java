package me.evrooij.services;

import me.evrooij.errors.ResponseError;
import me.evrooij.domain.User;
import me.evrooij.managers.UserManager;
import me.evrooij.util.JsonUtil;
import spark.Request;
import spark.Response;

import static me.evrooij.util.JsonUtil.json;
import static spark.Spark.*;

public class UserService {
    private UserManager userManager;

    public UserService() {
        userManager = new UserManager();

        get("/users", (request, response) -> userManager.getAllUsers(), json());

        get("/users", (req, res) -> userManager.getAllUsers(), json());

        get("/users/:id", (req, res) -> {
            String id = req.params(":id");
            User user = userManager.getUser(id);
            if (user != null) {
                return user;
            }
            res.status(400);
            return new ResponseError("No user with id %s found.", id);
        }, json());

        post("/users", (req, res) -> userManager.createUser(
                req.queryParams("name"),
                req.queryParams("email")
        ), json());

        put("/users/:id", (req, res) -> userManager.updateUser(
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
        if (exception instanceof IllegalArgumentException) {
            response.status(400);
            response.body(JsonUtil.toJson(new ResponseError(exception)));
        } else {
            response.body(JsonUtil.toJson(new ResponseError(exception)));
        }
    }
}
