package me.evrooij.services;

import me.evrooij.domain.Account;
import me.evrooij.managers.AccountManager;
import me.evrooij.util.JsonUtil;

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
//            String username = request.queryParams("username");
//            String email = request.queryParams("email");
//            String password = request.queryParams("password");
            String json = request.body();

            Account account = JsonUtil.accountFromJson(json);

            System.out.println(String.format("Body in string: %s", json));
            System.out.println(String.format("Account from json using gson: %s", account.toString()));

            Account returning = accountManager.registerAccount(account.getUsername(), account.getEmail(), account.getPassword());
            System.out.println(String.format("Returning: %s", returning));
            return returning;
        }, json());

        before(this::beforeRouteHandle);
        after(this::afterRouteHandle);
        exception(Exception.class, this::handleException);
    }
}
