package me.evrooij.services;

import com.google.gson.Gson;
import me.evrooij.domain.Account;
import me.evrooij.managers.AccountManager;
import me.evrooij.responses.ResponseMessage;

import java.util.Map;

import static me.evrooij.util.JsonUtil.json;
import static spark.Spark.*;

public class AccountService extends DefaultService {
    private AccountManager accountManager;

    public AccountService() {
        accountManager = new AccountManager();

        // Production env
        port(6438);

        get("/users/login", (request, response) -> {
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            return accountManager.getAccount(username, password);
        }, json());

        post("/users/register", (request, response) -> {
            String json = request.body();
            System.out.println(String.format("Received json from /users/register in req body: %s", json));

            Map<String, String> accountMap = new Gson().fromJson(request.body(), Map.class);
            String username = accountMap.get("username");
            String email = accountMap.get("email");
            String password = accountMap.get("password");
            System.out.println(String.format("Username: %s\nEmail: %s\nPassword: %s", username, email, password));

            Account returning = accountManager.registerAccount(username, email, password);
            System.out.println(String.format("Returning: %s", returning));
            return returning;
        }, json());

        get("/accounts/:accountId/friends/find", (request, response) -> {
            int accountId = Integer.valueOf(request.params(":accountId"));

            String searchQuery = request.queryParams("query");

            return accountManager.searchFriends(accountId, searchQuery);
        }, json());

        get("/accounts/:accountId/friends", (request, response) -> {
            int accountId = Integer.valueOf(request.params(":accountId"));

            return accountManager.getFriends(accountId);
        }, json());

        post("/accounts/:accountId/friends/add", (request, response) -> {
            int accountId = Integer.valueOf(request.params(":accountId"));

            String json = request.body();
            System.out.println(String.format("Received json from /accounts/:accountId/friends/add in req body: %s", json));

            Account friend = new Gson().fromJson(request.body(), Account.class);
            System.out.println(String.format("Received new friend: %s", friend.toString()));

            boolean result = accountManager.addFriend(accountId, friend);
            if (result) {
                return new ResponseMessage("Successfully added friend.");
            } else {
                return new ResponseMessage("Could not add friend.");
            }
        }, json());

        before(this::beforeRouteHandle);
        after(this::afterRouteHandle);
        exception(Exception.class, this::handleException);
    }
}
