package me.evrooij.daos;

import me.evrooij.data.Account;
import org.hibernate.Session;
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

    public Feedback get(int id) {
        entityManager.getTransaction().begin();
        Feedback list = entityManager.find(Feedback.class, id);
        entityManager.getTransaction().commit();
        return list;
    }

    public void delete(Feedback feedback) {
        Session session = getSession();

        session.delete(feedback);

        //This makes the pending delete to be done
        session.flush();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }
}
