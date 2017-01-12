package me.evrooij.daos;

import me.evrooij.data.Account;
import me.evrooij.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author eddy on 8-12-16.
 */

public class AccountDAO {
    private final EntityManager entityManager;

    public AccountDAO() {
        entityManager = HibernateUtil.getInstance().getEntityManager();
    }

    public Account create(String username, String email, String password) {
        Account account = new Account(username, email, password);
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.getTransaction().commit();
        return account;
    }

    /**
     * Get the password hash for a username.
     *
     * @param username username of the account
     * @return password hash of the account
     */
    public String getHash(String username) {
        String result;

        entityManager.getTransaction().begin();
        Query query = getSession().createQuery("SELECT a.password FROM Account a WHERE a.username = :username");
        query.setString("username", username);
        List resultList = query.getResultList();
        if (resultList == null) {
            result = null;
        } else if (resultList.size() == 0) {
            result = null;
        } else {
            result = (String) query.getResultList().get(0);
        }
        entityManager.getTransaction().commit();
        return result;
    }

    /**
     * Retrieves an account from the database if username/password combination is correct
     *
     * @param username username of the account
     * @param password password of the account
     * @return Account object
     */
    public Account get(String username, String password) {
        entityManager.getTransaction().begin();
        Query query = getSession().createQuery("SELECT a FROM Account a WHERE a.username = :username AND a.password = :password");
        query.setString("username", username);
        query.setString("password", password);
        Account account = (Account) query.uniqueResult();
        entityManager.getTransaction().commit();
        return account;
    }

    public Account get(int id) {
        entityManager.getTransaction().begin();
        Account account = entityManager.find(Account.class, id);
        entityManager.getTransaction().commit();
        return account;
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public int getAmount() {
        return ((Long) getSession().createQuery("select count(*) from Account").uniqueResult()).intValue();
    }

    /**
     * Removes an account from the database
     *
     * @param account account to remove
     */
    public void deleteAccount(Account account) {
        entityManager.getTransaction().begin();
        entityManager.remove(account);
        entityManager.getTransaction().commit();
    }

    /**
     * Returns all accounts from database
     *
     * @return a list of all accounts
     */
    public List<Account> getAccounts() {
        return getSession().createCriteria(Account.class).list();
    }

    /**
     * Adds a friend to the account with account id
     *
     * @param accountId
     * @param friend
     */
    @SuppressWarnings("JavaDoc")
    public void addFriend(int accountId, Account friend) {
        Account accountToAddTo = get(accountId);
        entityManager.getTransaction().begin();

        accountToAddTo.addFriend(friend);
        entityManager.merge(accountToAddTo);

        entityManager.getTransaction().commit();
    }

    /**
     * Checks if username exists
     *
     * @param username
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public boolean usernameExists(String username) {
        boolean result;

        entityManager.getTransaction().begin();
        Query query = getSession().createQuery("SELECT a.username FROM Account a WHERE a.username = :username");
        query.setString("username", username);
        List resultList = query.getResultList();
        if (resultList == null) {
            result = false;
        } else if (resultList.size() == 0) {
            result = false;
        } else {
            result = true;
        }
        entityManager.getTransaction().commit();
        return result;
    }
}
