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
            throw new Exception(String.format("List with id %s does not exist!", listId));
        }
        return groceryListDAO.addProduct(listId, product);
    }
}
