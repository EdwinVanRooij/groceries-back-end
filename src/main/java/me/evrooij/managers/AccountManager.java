package me.evrooij.managers;

import me.evrooij.daos.AccountDAO;
import me.evrooij.data.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountManager {

    private AccountDAO accountDAO;

    public AccountManager() {
        accountDAO = new AccountDAO();
    }

    /**
     * Retrieves the user's Account on id
     *
     * @return account object
     */
    @SuppressWarnings("JavaDoc")
    public Account getAccount(int id) {
        return accountDAO.getAccount(id);
    }

    /**
     * Retrieves the user's Account on a correct username with password combination.
     *
     * @param username globally unique username
     * @param password password for the user account
     * @return returns the account on correct login credentials, null on incorrect login credentials
     */
    @SuppressWarnings("JavaDoc")
    public Account getAccount(String username, String password) {
        return accountDAO.getAccount(username, password);
    }

    /**
     * Creates a new Account entry in the database.
     *
     * @param username identification name, must be:
     *                 unique relative to all of the other usernames
     *                 at least 6 characters long
     *                 at max 30 characters long
     *                 only alphanumeric characters
     * @param email    e-mail address of the user used for communication purposes, must:
     *                 contain something before the @
     *                 contain a @ character
     *                 contain something after the @
     *                 contain a dot after the @
     *                 contain something after the dot
     * @param password used to log into the user account for validation, must be:
     *                 at least 8 characters long
     *                 at max 100 characters long
     * @return the user account if successful, null if unsuccessful
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
     * Removes an account from the database if:
     * - the username/password combination is correct
     * - the account exists
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

    /**
     * Searches for an account that somewhat matches the search query word
     *
     * @param searcherId  account id of the searcher, exclude this one
     * @param searchQuery the user entered search query, matches on:
     *                    (all of these are case insensitive)
     *                    - query equals username
     *                    - query equals email
     *                    - query partially equals username
     *                    - query partially equals email
     *                    Does not match:
     *                    - own account
     *                    - accounts which are already friends
     * @return a list with accounts that match the search query
     */
    public List<Account> searchFriends(int searcherId, String searchQuery) {
        Account searcher = getAccount(searcherId);

        List<Account> matchList = new ArrayList<>();
        for (Account a : accountDAO.getAccounts()) {
            // If searcher is already friends with, ignore
            if (searcher.isFriendsWith(a.getId())) {
                continue;
            }

            // If account matches the search query
            if (a.matchesFriendSearch(searcherId, searchQuery)) {
                matchList.add(a);
            }
        }
        return matchList;
    }

    /**
     * Adds friend to account of accountId, if friend is not already a friend of account
     *
     * @param accountId account to add friend to
     * @param friend    account to add as new friend
     * @return value indicating success or failure
     */
    public boolean addFriend(int accountId, Account friend) {
        Account account = accountDAO.getAccount(accountId);
        if (account.isFriendsWith(friend.getId())) {
            return false;
        }
        accountDAO.addFriend(accountId, friend);
        return true;
    }

    /**
     * Returns all the friends of an account if the accountId is associated with a valid account
     *
     * @param accountId account id of the account to search friends for
     * @return list of account objects if accountId was valid, null if it wasn't
     */
    public List<Account> getFriends(int accountId) {
        Account account = accountDAO.getAccount(accountId);
        if (account != null) {
            return account.getFriends();
        }
        return null;
    }
}
