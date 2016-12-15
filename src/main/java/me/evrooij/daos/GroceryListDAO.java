package me.evrooij.daos;

import me.evrooij.domain.Account;
import me.evrooij.domain.GroceryList;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

/**
 * @author eddy on 8-12-16.
 */

public class GroceryListDAO {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GroceriesPersistenceUnit");
    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    public GroceryListDAO() {
    }

    /**
     * Creates a new GroceryList
     *
     * @param name
     * @param owner
     * @return newly created list
     */
    @SuppressWarnings("JavaDoc")
    public GroceryList create(String name, Account owner) {
        GroceryList groceryList = new GroceryList(name, owner);
        entityManager.getTransaction().begin();
        entityManager.persist(groceryList);
        entityManager.getTransaction().commit();
        return groceryList;
    }

    /**
     * Returns all GroceryLists an account is in
     *
     * @param id unique identifier for the account
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public List<GroceryList> getLists(int id) {
        entityManager.getTransaction().begin();

        List<GroceryList> lists = entityManager.createQuery("SELECT l FROM GroceryList l").getResultList();

        entityManager.getTransaction().commit();
        return lists;
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }
}
