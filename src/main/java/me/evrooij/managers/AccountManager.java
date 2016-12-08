package me.evrooij.managers;

import me.evrooij.domain.Account;
import me.evrooij.errors.InvalidAccountException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class AccountManager {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NewPersistenceUnit");

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

    /**
     * Example of save to database, should actually work already lmao, clean up tho
     *
     * @param account
     */
    public void saveToDatabase(Account account) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.getTransaction().commit();
        entityManager.close();
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
