package me.evrooij.managers;

import com.sun.istack.internal.Nullable;
import me.evrooij.daos.GroceryListDAO;
import me.evrooij.data.Account;
import me.evrooij.data.GroceryList;
import me.evrooij.data.Product;

import java.util.List;

public class GroceryListManager {

    private GroceryListDAO groceryListDAO;

    public GroceryListManager() {
        groceryListDAO = new GroceryListDAO();
    }

    /**
     * Creates a GroceryList
     *
     * @param name         name of the list
     * @param owner        initial creator of the list
     * @param participants list of accounts participating in this list
     *                     if left null or length 0, don't use it
     * @return the grocery list with or without participants depending on value given to participants
     */
    public GroceryList createGroceryList(String name, Account owner, @Nullable List<Account> participants) {
        if (participants == null || participants.size() == 0) {
            // If participants was not filled in correctly, ignore it
            return groceryListDAO.create(name, owner);
        }
        // Use participants
        return groceryListDAO.create(name, owner, participants);
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

    /**
     * Updates product with id productId from list listId, if they're both valid
     *
     * @param listId    id of list to update product from
     * @param productId id of product to update
     * @param product   updated product object
     * @return true if succeeded, false if not
     */
    public boolean updateProduct(int listId, int productId, Product product) {
        GroceryList list = groceryListDAO.getList(listId);
        if (list == null) {
            // There is no such list, return false
            return false;
        }
        if (list.getProduct(productId) == null) {
            // There is no such product in this list, return false
            return false;
        }
        // Update product, return true
        list.updateProduct(productId, product.getName(), product.getAmount(), product.getComment(), product.getOwner());
        groceryListDAO.update(list);
        return true;
    }
}
