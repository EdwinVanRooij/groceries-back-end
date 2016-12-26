package me.evrooij.managers;

import me.evrooij.daos.AccountDAO;
import me.evrooij.daos.GroceryListDAO;
import me.evrooij.domain.Account;
import me.evrooij.domain.GroceryList;
import me.evrooij.domain.Product;

import java.util.List;

public class GroceryListManager {

    private GroceryListDAO groceryListDAO;

    public GroceryListManager() {
        groceryListDAO = new GroceryListDAO();
    }

    /**
     * Creates a GroceryList
     *
     * @param name
     * @param owner
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public GroceryList createGroceryList(String name, Account owner) {
        return groceryListDAO.create(name, owner);
    }

    /**
     * Returns all grocery lists of a given account id
     *
     * @param accountId
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public List<GroceryList> getListsByAccountId(int accountId) {
        return groceryListDAO.getLists(accountId);
    }

    /**
     * Returns one list by id
     *
     * @param listId
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public GroceryList getList(int listId) {
        return groceryListDAO.getList(listId);
    }

    /**
     * Adds a product to a list if the list exists
     *
     * @param listId  id of the list to add the product to
     * @param product product to add
     * @return the product if operation was successful, null if it wasn't
     */
    public Product addProduct(int listId, Product product) throws Exception {
        if (groceryListDAO.getList(listId) == null) {
//            throw new Exception(String.format("List with id %s does not exist!", listId));
            return null;
        }
        return groceryListDAO.addProduct(listId, product);
    }

    /**
     * Deletes a product by id from the list by list id
     *
     * @param listId    id of the list to delete product from
     * @param productId id of product to delete
     * @return true if operation succeeded, false if it didn't
     */
    public boolean deleteProduct(int listId, int productId) {
        GroceryList list = groceryListDAO.getList(listId);
        if (list == null) {
//            throw new Exception(String.format("List with id %s does not exist!", listId));
            return false;
        }
        Product product = list.getProduct(productId);
        if (product == null) {
//            throw new Exception(String.format("Product with id %s does not exist!", productId));
            return false;
        }
        groceryListDAO.deleteProduct(listId, productId);
        return true;
    }

    /**
     * Adds a new participant to a list
     * listId must point to an existent list
     * new participant must not be the owner
     * new participant must not already be in the list
     *
     * @param listId         id of the list to add participant to
     * @param newParticipant new participant to join the list
     * @return success if succeeded, false if not
     */
    public boolean addParticipant(int listId, Account newParticipant) {
        GroceryList list = groceryListDAO.getList(listId);
        if (list == null) {
            // List doesn't exist, return false
            return false;
        }
        if (list.getOwner().equals(newParticipant)) {
            // New participant is the owner, return false
            return false;
        }
        if (list.hasParticipant(newParticipant)) {
            // Trying to add a user who's already in the list, return false
            return false;
        }
        list.addParticipant(newParticipant);
        groceryListDAO.update(list);
        return true;
    }
}
