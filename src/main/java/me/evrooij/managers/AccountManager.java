package me.evrooij.managers;

import me.evrooij.daos.AccountDAO;
import me.evrooij.domain.Account;

public class AccountManager {

    private AccountDAO accountDAO;

    public AccountManager() {
        accountDAO = new AccountDAO();
    }

    /**
     * Returns an account
     *
     * @param username
     * @param password
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public Account getAccount(String username, String password) {
        return accountDAO.getAccount(username, password);
    }

    /**
     * Registers an account
     *
     * @param username
     * @param email
     * @param password
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public Account registerAccount(String username, String email, String password) {
        String regexUsername = "^[a-zA-Z0-9]{6,30}$";
        String regexEmail = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\.?[a-zA-Z0-9]*$";
        String regexPassword = "^.{8,100}$";

        // Match regular expressions with the user input
        if (!username.matches(regexUsername) || !email.matches(regexEmail) || !password.matches(regexPassword)) {
            // One of the parameters was invalid
            return null;
        }
        return accountDAO.register(username, email, password);
    }

    /**
     * Removes an account from the database if the username/password combination is correct
     *
     * @param username
     * @param password
     * @return boolean value indicating the exit status of removal
     */
    @SuppressWarnings("JavaDoc")
    public boolean removeAccount(String username, String password) {
        if (accountDAO.getAccount(username, password) != null) {
            accountDAO.deleteAccount(username, password);
            return true;
        }
        return false;
    }

}
