package me.evrooij.daos;

import me.evrooij.data.Account;

import javax.persistence.Query;

import me.evrooij.data.GroceryList;
import org.hibernate.Session;
import me.evrooij.data.Feedback;
import me.evrooij.util.HibernateUtil;

import javax.persistence.EntityManager;
import java.util.List;

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

    public List<Feedback> getAll() {
        entityManager.getTransaction().begin();

        Query query = entityManager.createQuery(
                "SELECT l FROM Feedback l ");
        List<Feedback> lists = query.getResultList();

        entityManager.getTransaction().commit();
        return lists;
    }

    public Feedback get(int id) {
        entityManager.getTransaction().begin();
        Feedback list = entityManager.find(Feedback.class, id);
        entityManager.getTransaction().commit();
        return list;
    }

    public void delete(Feedback feedback) {
        entityManager.getTransaction().begin();
        entityManager.remove(feedback);
        entityManager.getTransaction().commit();
    }
}
