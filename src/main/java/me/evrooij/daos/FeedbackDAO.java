package me.evrooij.daos;

import me.evrooij.domain.Account;
import me.evrooij.domain.Feedback;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author eddy on 8-12-16.
 */

public class FeedbackDAO {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GroceriesPersistenceUnit");
    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    public FeedbackDAO() {
    }

    public Feedback create(String message, Feedback.Type type, Account sender) {
        Feedback feedback = new Feedback(message, type, sender);
        entityManager.getTransaction().begin();
        entityManager.persist(feedback);
        entityManager.getTransaction().commit();
        return feedback;
    }

}
