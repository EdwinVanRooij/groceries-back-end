package me.evrooij.managers;

import me.evrooij.daos.AccountDAO;
import me.evrooij.domain.Account;

public class AccountManager {

    private AccountDAO accountDAO;

    public AccountManager() {
        accountDAO = new AccountDAO();
    }

    public Account getAccount(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'username' cannot be empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'password' cannot be empty");
        }

        return accountDAO.getAccount(username, password);
    }

    public Account registerAccount(String username, String email, String password) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'name' cannot be empty");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'email' cannot be empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'password' cannot be empty");
        }

        return accountDAO.register(username, email, password);
    }
}
