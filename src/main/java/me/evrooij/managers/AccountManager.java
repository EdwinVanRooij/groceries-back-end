package me.evrooij.managers;

import me.evrooij.domain.Account;
import me.evrooij.domain.User;
import me.evrooij.errors.InvalidAccountException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountManager {

    // temporary, will get from database later on
    private List<Account> accounts;

    public AccountManager() {
        accounts = new ArrayList<>();
    }

    public Account getAccount(String username, String password) throws InvalidAccountException {
        failIfInvalid(username, password);

        // todo: get account from database
        for (Account account :
                accounts) {
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                return account;
            }
        }
        throw new InvalidAccountException("No account found with this username/password combination");
    }

    public Account registerAccount(String username, String email, String password) throws InvalidAccountException {
        failIfInvalid(username, email, password);

        Account account = new Account(username, email, password);
        // todo: persist account to database

        accounts.add(account);
        return account;
    }

    private void failIfInvalid(String name, String email) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'name' cannot be empty");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'email' cannot be empty");
        }
    }

    private void failIfInvalid(String name, String email, String password) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'name' cannot be empty");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'email' cannot be empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'password' cannot be empty");
        }
    }
}
