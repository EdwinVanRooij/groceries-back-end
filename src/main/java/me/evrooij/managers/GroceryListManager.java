package me.evrooij.managers;

import me.evrooij.daos.GroceryListDAO;
import me.evrooij.daos.ProductDAO;
import me.evrooij.data.Account;
import me.evrooij.data.GroceryList;
import me.evrooij.data.Product;
import me.evrooij.exceptions.InvalidParticipantException;

import java.util.List;

public class GroceryListManager {

    private GroceryListDAO groceryListDAO;
    private ProductDAO productDAO;

    private String EXCEPTION_LIST_DOES_NOT_EXIST = "List with id %s does not exist.";
    private String EXCEPTION_PRODUCT_DOES_NOT_EXIST = "Product with id %s does not exist.";

    public GroceryListManager() {
        groceryListDAO = new GroceryListDAO();
        productDAO = new ProductDAO();
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
    public GroceryList createGroceryList(String name, Account owner, List<Account> participants) {
        if (participants == null || participants.size() == 0) {
            // If participants was not filled in correctly, ignore it
            return groceryListDAO.create(name, owner, null);
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
        GroceryList list = groceryListDAO.getList(listId);
        if (list == null) {
            throw new NullPointerException(String.format(EXCEPTION_LIST_DOES_NOT_EXIST, listId));
        }
        return groceryListDAO.getList(listId);
    }

    /**
     * Adds a product to a list if the list exists
     *
     * @param listId  id of the list to add the product to
     * @param product product to add
     * @return the product if operation was successful, null if it wasn't
     */
    public Product addProduct(int listId, Product product) {
        if (groceryListDAO.getList(listId) == null) {
            throw new NullPointerException(String.format(EXCEPTION_LIST_DOES_NOT_EXIST, listId));
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
            throw new NullPointerException(String.format(EXCEPTION_LIST_DOES_NOT_EXIST, listId));
        }
        Product product = list.getProduct(productId);
        if (product == null) {
            throw new NullPointerException(String.format(EXCEPTION_PRODUCT_DOES_NOT_EXIST, productId));
        }
        groceryListDAO.deleteProduct(listId, productId);
        productDAO.updateDeletionTime(product);
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
    public boolean addParticipant(int listId, Account newParticipant) throws InvalidParticipantException {
        GroceryList list = groceryListDAO.getList(listId);
        if (list == null) {
            // List doesn't exist, return false
            throw new NullPointerException(String.format(EXCEPTION_LIST_DOES_NOT_EXIST, listId));
        }
        if (list.getOwner().equals(newParticipant)) {
            // New participant is the owner, return false
            throw new InvalidParticipantException("New participant must not be the owner.");
        }
        if (list.hasParticipant(newParticipant)) {
            // Trying to add a user who's already in the list, return false
            throw new InvalidParticipantException("New participant is already in the list");
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
            throw new NullPointerException(String.format(EXCEPTION_LIST_DOES_NOT_EXIST, listId));
        }
        if (list.getProduct(productId) == null) {
            // There is no such product in this list, return false
            throw new NullPointerException(String.format(EXCEPTION_PRODUCT_DOES_NOT_EXIST, productId));
        }
        // Update product, return true
        list.updateProduct(productId, product.getName(), product.getAmount(), product.getComment(), product.getOwner());
        groceryListDAO.update(list);
        return true;
    }
}
