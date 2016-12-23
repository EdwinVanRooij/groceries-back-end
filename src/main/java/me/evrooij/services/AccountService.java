package me.evrooij.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.evrooij.domain.Account;
import me.evrooij.managers.AccountManager;
import me.evrooij.util.JsonUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static me.evrooij.util.JsonUtil.json;
import static spark.Spark.*;

public class AccountService extends DefaultService {
    private AccountManager accountManager;

    public AccountService() {
        accountManager = new AccountManager();

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

        before(this::beforeRouteHandle);
        after(this::afterRouteHandle);
        exception(Exception.class, this::handleException);
    }
}
