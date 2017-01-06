package me.evrooij.daos;

import me.evrooij.data.Account;
import me.evrooij.data.GroceryList;
import me.evrooij.data.Product;
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
     * @param participants
     * @return newly created list
     */
    @SuppressWarnings("JavaDoc")
    public GroceryList create(String name, Account owner, List<Account> participants) {
        GroceryList groceryList = new GroceryList(name, owner, participants);
        entityManager.getTransaction().begin();
        entityManager.persist(groceryList);
        entityManager.getTransaction().commit();
        return groceryList;
    }

    /**
     * Returns all GroceryLists an account is in, this includes where the account is owner and where it's a participant
     *
     * @param accountId id of the account
     * @return
     */
    @SuppressWarnings({"JavaDoc", "unchecked"})
    public List<GroceryList> getLists(int accountId) {
        entityManager.getTransaction().begin();

        Query query = entityManager.createQuery(
                "SELECT l FROM GroceryList l " +
                        "WHERE owner.id = :id " +
                        "OR :id IN(SELECT id FROM l.participants)");
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
     * @param accountId account id of the user to search for
     * @return integer value
     */
    public int getAmountOfListsForUser(int accountId) {
        return getLists(accountId).size();
    }

    /**
     * Returns a list by id
     *
     * @param listId
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public GroceryList getList(int listId) {
        entityManager.getTransaction().begin();
        GroceryList list = entityManager.find(GroceryList.class, listId);
        entityManager.getTransaction().commit();

        return list;
    }

    /**
     * Adds a product to list
     *
     * @param listId
     * @param product
     * @return the created product
     */
    @SuppressWarnings("JavaDoc")
    public Product addProduct(int listId, Product product) {
        GroceryList list = getList(listId);

        entityManager.getTransaction().begin();
        list.addProduct(product.getName(), product.getAmount(), product.getComment(), product.getOwner());
        entityManager.merge(list);
        entityManager.getTransaction().commit();
        return list.getProduct(product.getName(), product.getOwner(), product.getComment());
    }

    /**
     * Deletes a product from a list
     *
     * @param listId    list to delete product from
     * @param productId product to delete
     */
    public void deleteProduct(int listId, int productId) {
        GroceryList list = getList(listId);

        entityManager.getTransaction().begin();
        list.removeItem(productId);
        entityManager.merge(list);
        entityManager.getTransaction().commit();
    }

    /**
     * Updates the list to reflect current state
     *
     * @param list list to update in database
     */
    public void update(GroceryList list) {
        entityManager.getTransaction().begin();
        getSession().merge(list);
        entityManager.getTransaction().commit();
    }
}
