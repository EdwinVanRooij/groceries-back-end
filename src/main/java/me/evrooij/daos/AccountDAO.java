package me.evrooij.daos;

import me.evrooij.domain.Account;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.*;

/**
 * @author eddy on 8-12-16.
 */

public class AccountDAO {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GroceriesPersistenceUnit");
    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    public AccountDAO() {
    }

    /**
     * Registers a new account
     *
     * @param username
     * @param email
     * @param password
     * @return newly created account
     */
    @SuppressWarnings("JavaDoc")
    public Account register(String username, String email, String password) {
        Account account = new Account(username, email, password);
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.getTransaction().commit();
        return account;
    }

    /**
     * Retrieves an account from the database if username/password combination is correct
     *
     * @param username username of the account
     * @param password password of the account
     * @return Account object
     */
    public Account getAccount(String username, String password) {
        entityManager.getTransaction().begin();
        Query query = getSession().createQuery("SELECT a FROM Account a WHERE a.username = :username AND a.password = :password");
        query.setString("username", username);
        query.setString("password", password);
        Account account = (Account) query.uniqueResult();
        entityManager.getTransaction().commit();
        return account;
    }

    /**
     * Retrieves an account from the database if username/password combination is correct
     *
     * @param id id of the account
     * @return Account object
     */
    public Account getAccount(int id) {
        entityManager.getTransaction().begin();
        Query query = getSession().createQuery("SELECT a FROM Account a WHERE a.id = :id");
        query.setInteger("id", id);
        Account account = (Account) query.uniqueResult();
        entityManager.getTransaction().commit();
        return account;
    }

    /**
     * Gets the amount of accounts in the database
     *
     * @return integer value
     */
    public int getAmountOfAccounts() {
        return ((Long) getSession().createQuery("select count(*) from Account").uniqueResult()).intValue();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * Removes an account from the database
     *
     * @param username
     * @param password
     */
    @SuppressWarnings("JavaDoc")
    public void deleteAccount(String username, String password) {
        entityManager.getTransaction().begin();
        Query query = getSession().createQuery("DELETE FROM Account a WHERE a.username = :username AND a.password = :password");
        query.setString("username", username);
        query.setString("password", password);
        entityManager.getTransaction().commit();
    }
}
