package me.evrooij.managers;

import me.evrooij.daos.AccountDAO;
import me.evrooij.data.Account;
import me.evrooij.exceptions.DuplicateUsernameException;
import me.evrooij.exceptions.InstanceDoesNotExistException;
import me.evrooij.exceptions.InvalidFriendRequestException;
import me.evrooij.exceptions.InvalidLoginCredentialsException;
import me.evrooij.util.HashUtil;

import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
    private Account getAccount(int id) {
        return accountDAO.get(id);
    }

    /**
     * Retrieves the user's Account on a correct username with password combination.
     *
     * @param username globally unique username
     * @param password password for the user account
     * @return returns the account on correct login credentials, null on incorrect login credentials
     */
    @SuppressWarnings("JavaDoc")
    public Account login(String username, String password) throws InvalidLoginCredentialsException, InvalidKeySpecException, NoSuchAlgorithmException {
        return validateLoginCredentials(username, password);
    }

    /**
     * Returns an account on correct login credentials
     *
     * @param username
     * @param password
     * @return
     * @throws InvalidLoginCredentialsException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    @SuppressWarnings("JavaDoc")
    private Account validateLoginCredentials(String username, String password) throws InvalidLoginCredentialsException, InvalidKeySpecException, NoSuchAlgorithmException {
        String hash = accountDAO.getHash(username);
        if (hash == null) {
            throw new InvalidLoginCredentialsException("Incorrect login credentials.");
        }

        if (HashUtil.validatePassword(password, hash)) {
            // Password was correct, return account
            return accountDAO.get(username, hash);
        } else {
            throw new InvalidLoginCredentialsException("Incorrect login credentials.");
        }
    }

    /**
     * Creates a new Account entry in the database.
     *
     * @param username identification name, must be:
     *                 unique relative to all of the other usernames
     *                 at least 2 characters long
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
    public Account registerAccount(String username, String email, String password) throws DuplicateUsernameException {
        String regexUsername = "^[a-zA-Z0-9]{2,30}$";
        String regexEmail = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\.?[a-zA-Z0-9]*$";
        String regexPassword = "^.{8,100}$";

        // Check if username already exists
        if (accountDAO.usernameExists(username)) {
            // Username exists
            throw new DuplicateUsernameException(String.format("Username %s already exists.", username));
        }

        // Match regular expressions with the user input
        if (!username.matches(regexUsername)) {
            // Invalid username
            throw new InvalidParameterException("Username must contain only 2 to 30 alphanumeric characters.");
        }

        if (!email.matches(regexEmail)) {
            // Invalid email
            throw new InvalidParameterException("Please enter a valid email address.");
        }

        if (!password.matches(regexPassword)) {
            // Invalid password
            throw new InvalidParameterException("Password must be at least 8 characters.");
        }

        // Hash password
        try {
            password = HashUtil.createHash(password);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return accountDAO.create(username, email, password);
    }

    /**
     * Removes an account from the database if:
     * - the username/password combination is correct
     * - the account exists
     *
     * @param username
     * @param password
     * @return true if account was deleted, false if it wasn't
     * @throws InvalidLoginCredentialsException on incorrect login credentials
     */
    @SuppressWarnings("JavaDoc")
    public boolean removeAccount(String username, String password) throws InvalidLoginCredentialsException, InvalidKeySpecException, NoSuchAlgorithmException {
        Account a = validateLoginCredentials(username, password);
        accountDAO.deleteAccount(a);
        return true;
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
     * Both accounts are friends with each other now.
     *
     * @param accountId account to add friend to
     * @param friend    account to add as new friend
     * @return value indicating success or failure
     * @throws InstanceDoesNotExistException if the friend account doesn't exist
     * @throws InvalidFriendRequestException if they're already friends
     */
    public boolean addFriend(int accountId, Account friend) throws InstanceDoesNotExistException, InvalidFriendRequestException {
        Account account = accountDAO.get(accountId);
        if (account == null) {
            // If account doesn't even exist, throw exception
            throw new InstanceDoesNotExistException(String.format("Account with id %s doesn't exist in database", accountId));
        }

        Account friendAccount = accountDAO.get(friend.getId());
        if (friendAccount == null) {
            // If friend doesn't exist, throw exception
            throw new InstanceDoesNotExistException(String.format("Friend account %s does not exist in database", friend.toString()));
        }


        if (account.isFriendsWith(friend.getId())) {
            // Already friends, don't add again
            System.out.println(String.format("Account %s is already friends with account with id %s", friend.toString(), accountId));
            throw new InvalidFriendRequestException(String.format("Account %s is already friends with account with id %s", friend.toString(), accountId));
        }

        // Add new friend to initiating user
        accountDAO.addFriend(accountId, friend);

        // Add initiating user to new friend as well
        accountDAO.addFriend(friend.getId(), account);

        return true;
    }

    /**
     * Returns all the friends of an account if the accountId is associated with a valid account
     *
     * @param accountId account id of the account to search friends for
     * @return list of account objects if accountId was valid, null if it wasn't
     */
    public List<Account> getFriends(int accountId) {
        Account account = accountDAO.get(accountId);
        if (account != null) {
            return account.getFriends();
        }
        return null;
    }
}
