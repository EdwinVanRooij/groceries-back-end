package me.evrooij.managers;

import me.evrooij.daos.AccountDAO;
import me.evrooij.domain.Account;

public class AccountManager {

    private AccountDAO accountDAO;

    public AccountManager() {
        accountDAO = new AccountDAO();
    }

    public Account getAccount(String username, String password) {
        return accountDAO.getAccount(username, password);
    }

    public Account registerAccount(String username, String email, String password) {
        return accountDAO.register(username, email, password);
    }
}
