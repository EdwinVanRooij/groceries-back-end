package me.evrooij.managers;

import me.evrooij.daos.AccountDAO;
import me.evrooij.daos.GroceryListDAO;
import me.evrooij.domain.Account;
import me.evrooij.domain.GroceryList;

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

}
