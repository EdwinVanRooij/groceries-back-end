package me.evrooij.services;

import me.evrooij.domain.Account;
import me.evrooij.errors.ResponseError;
import me.evrooij.managers.AccountManager;
import me.evrooij.util.JsonUtil;
import spark.Request;
import spark.Response;

import static me.evrooij.util.JsonUtil.json;
import static spark.Spark.*;

public class AccountService {
    private AccountManager accountManager;

    public AccountService() {
        accountManager = new AccountManager();

        get("/users/login", (request, response) -> {
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            return accountManager.getAccount(username, password);
        }, json());

        post("/users/register", (request, response) -> {
//            String username = request.queryParams("username");
//            String email = request.queryParams("email");
//            String password = request.queryParams("password");
            String json = request.body();

            Account account = JsonUtil.accountFromJson(json);

            System.out.println(String.format("Body in string: %s", json));
            System.out.println(String.format("Account from json using gson: %s", account.toString()));

            return accountManager.registerAccount(account.getUsername(), account.getEmail(), account.getPassword());
        }, json());

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
        response.type("application/json");
        response.body(JsonUtil.toJson(new ResponseError(exception)));
    }
}
