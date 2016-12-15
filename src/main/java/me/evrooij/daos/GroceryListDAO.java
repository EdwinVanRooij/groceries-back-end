package me.evrooij.daos;

import me.evrooij.domain.Account;
import me.evrooij.domain.GroceryList;
import me.evrooij.domain.Product;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
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
     * @param accountId unique identifier for the account
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public List<GroceryList> getLists(int accountId) {
        entityManager.getTransaction().begin();

        Query query = entityManager.createQuery("SELECT l FROM GroceryList l WHERE owner.id = :id");
        query.setParameter("id", accountId);
        List<GroceryList> lists = query.getResultList();

        entityManager.getTransaction().commit();
        return lists;
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * Return the amount of lists the user is in
     *
     * @param id account id of the user to search for
     * @return integer value
     */
    public int getAmountOfListsForUser(int id) {
        return getLists(id).size();
    }

}
