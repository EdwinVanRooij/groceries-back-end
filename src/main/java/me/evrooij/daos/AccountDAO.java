package me.evrooij.daos;

import me.evrooij.domain.Account;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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
    private final EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;

    public AccountDAO() {
        entityManagerFactory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public Account register(String username, String email, String password) {
        Account account = new Account(username, email, password);
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.getTransaction().commit();
        entityManager.close();
        return account;
    }

    public Account getAccount(String username, String password) {
        Session session = entityManager.unwrap(Session.class);
        entityManager.getTransaction().begin();
        Query query = session.getNamedQuery("Account.findByUsernameAndPassword");
        query.setString("username", username);
        query.setString("password", password);
        return (Account) query.uniqueResult();
    }
}
