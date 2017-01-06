package me.evrooij.daos;

import me.evrooij.data.Account;
import me.evrooij.data.Feedback;
import me.evrooij.util.HibernateUtil;

import javax.persistence.EntityManager;

/**
 * @author eddy on 8-12-16.
 */

public class FeedbackDAO {
    private final EntityManager entityManager;

    public FeedbackDAO() {
        entityManager = HibernateUtil.getInstance().getEntityManager();
    }

    public Feedback create(String message, Feedback.Type type, Account sender) {
        Feedback feedback = new Feedback(message, type, sender);
        entityManager.getTransaction().begin();
        entityManager.persist(feedback);
        entityManager.getTransaction().commit();
        return feedback;
    }

}
