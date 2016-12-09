package me.evrooij.daos;

import me.evrooij.domain.Account;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.*;

/**
 * @author eddy on 8-12-16.
 */

@NamedQueries({
// Query to select an account on valid username/password combination.
        @NamedQuery(
                name = "Account.findByUsernameAndPassword",
                query = "SELECT a FROM Account a WHERE a.username = :username AND a.password = :password"),
})
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
        entityManager.close();
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
        Session session = entityManager.unwrap(Session.class);
        entityManager.getTransaction().begin();
        Query query = session.getNamedQuery("Account.findByUsernameAndPassword");
        query.setString("username", username);
        query.setString("password", password);
        return (Account) query.uniqueResult();
    }

    /**
     * Gets the amount of accounts in the database
     *
     * @return integer value
     */
    public int getAmountOfAccounts() {
        // todo: implement
        return 0;
    }
}
