package me.evrooij.services;

import com.google.gson.Gson;
import me.evrooij.data.Account;
import me.evrooij.managers.AccountManager;
import me.evrooij.data.ResponseMessage;
import me.evrooij.util.JsonUtil;

import java.util.List;
import java.util.Map;

import static me.evrooij.Config.*;
import static me.evrooij.util.JsonUtil.json;
import static spark.Spark.*;

public class AccountService {
    private AccountManager accountManager;

    public AccountService() {
        accountManager = new AccountManager();

        get(PATH_LOGIN, (request, response) -> {
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            return accountManager.getAccount(username, password);
        }, json());

        post(PATH_REGISTER, (request, response) -> {
            String json = request.body();
            System.out.println(String.format("Received json from /users/register in req body: %s", json));

            @SuppressWarnings("unchecked") Map<String, String> accountMap = new Gson().fromJson(request.body(), Map.class);
            String username = accountMap.get("username");
            String email = accountMap.get("email");
            String password = accountMap.get("password");
            System.out.println(String.format("Username: %s\nEmail: %s\nPassword: %s", username, email, password));

            Account returning = accountManager.registerAccount(username, email, password);
            System.out.println(String.format("Returning: %s", returning));
            return returning;
        }, json());

        get(PATH_FIND_FRIEND, (request, response) -> {
            int accountId = Integer.valueOf(request.params(":accountId"));

            String searchQuery = request.queryParams("query");

            return accountManager.searchFriends(accountId, searchQuery);
        }, json());

        get(PATH_GET_FRIENDS, (request, response) -> {
            int accountId = Integer.valueOf(request.params(":accountId"));

            System.out.println(String.format("Searching for friends of account %s", accountId));
            List<Account> accountList = accountManager.getFriends(accountId);
            if (accountList != null) {
                System.out.println(String.format("returning an account list with %s accounts", accountList.size()));
            } else {
                System.out.println("Accountlist is null");
            }
            return accountList;
        }, json());

        post(PATH_ADD_FRIEND, (request, response) -> {
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

        after((request, response) -> response.type("application/json"));

        exception(Exception.class, (exception, request, response) -> {
            response.status(400);
            response.type("application/json");
            response.body(JsonUtil.toJson(new ResponseMessage(exception)));
        });
    }
}
