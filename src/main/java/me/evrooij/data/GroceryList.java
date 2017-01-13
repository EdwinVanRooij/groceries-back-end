package me.evrooij.data;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eddy
 * Date: 20-11-16.
 */

@Entity
public class GroceryList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private int id;
    @Expose
    private String name;

    @Expose
    @ManyToOne// A list can have one owner, an account can have multiple list
    private Account owner;

    @Expose
    @ManyToMany// A list can have multiple participants, a user can be in multiple lists
    private List<Account> participants;

    @Expose
    @OneToMany(cascade = CascadeType.ALL)// A list can have multiple products, a product belongs to one list
    private List<Product> productList;

    /**
     * Unique identifier
     *
     * @return int id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of this list
     *
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the owner of this list, it's the user who created this list originally
     *
     * @return Account object of the owner
     */
    public Account getOwner() {
        return owner;
    }

    /**
     * Get all participants of this list
     *
     * @return list of accounts
     */
    public List<Account> getParticipants() {
        return participants;
    }

    /**
     * A list of product objects
     *
     * @param name         name of the list
     * @param owner        account of the user who created this list
     * @param participants list of accounts who are participating in this list, may be null
     */
    public GroceryList(String name, Account owner, List<Account> participants) {
        this.name = name;
        this.owner = owner;

        if (participants == null) {
            // Init new list
            this.participants = new ArrayList<>();
        } else {
            // Set existing list of initial participants
            this.participants = participants;
        }

        productList = new ArrayList<>();
    }

    public GroceryList() {
    }

    /**
     * Adds someone else to this list
     *
     * @param account new account to add as participant
     *                must not already be in the list of participants
     * @return boolean value indicating the exit status
     */
    public boolean addParticipant(Account account) {
        for (Account participant : participants) {
            if (participant.equals(account)) {
                // Account is already a participant, do not allow to re-add
                return false;
            }
        }
        participants.add(account);
        return true;
    }

    /**
     * Adds a new product to the list
     *
     * @param name    name of the product
     * @param amount  amount of times you want the product
     * @param comment comment about the product
     * @param owner   user who added this product
     */
    public void addProduct(String name, int amount, String comment, Account owner) {
        productList.add(new Product(name, amount, comment, owner));
    }

    /**
     * Checks if the given account is already a participant in this list
     *
     * @param account account to check for
     * @return true if the account is already a participant here, false if it's not
     */
    public boolean hasParticipant(Account account) {
        for (Account participant : participants) {
            if (participant.equals(account)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Edits an existing item of this list
     *
     * @param id      unique identifier of the product
     * @param name    new name of the product
     * @param amount  new amount of the product
     * @param comment new comment of the product
     * @param owner   must equal the owner of this product
     *                you're not allowed to edit the product of someone else
     * @return boolean indicating the exit status of the method
     */
    public boolean editItem(int id, String name, int amount, String comment, Account owner) {
        Product product = getProduct(id);
        if (product.getOwner().equals(owner)) {
            product.setName(name);
            product.setAmount(amount);
            product.setComment(comment);
            return true;
        }
        return false;
    }

    /**
     * Removes a product
     *
     * @param id unique identifier of the product to remove
     */
    public void removeItem(int id) {
        productList.remove(getProduct(id));
    }

    /**
     * Returns the amount of products currently in the list
     *
     * @return integer value indicating the size of the product list
     */
    public int getAmountOfProducts() {
        return productList.size();
    }

    /**
     * Returns the amount of participants in this list, excluding owner
     *
     * @return integer value
     */
    public int getAmountOfParticipants() {
        return participants.size();
    }

    /**
     * Searches the GroceryList for a product
     *
     * @param id id of the product to look for
     * @return the product if it's in here, null if it's not
     */
    public Product getProduct(int id) {
        for (Product p :
                productList) {
            if (p.getId() == id) {
                // Product found
                return p;
            }
        }
        // No product found
        return null;
    }

    /**
     * Returns a product
     *
     * @param name
     * @param owner
     * @param comment
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public Product getProduct(String name, Account owner, String comment) {
        for (Product p : productList) {
            if (p.getName().equals(name) && p.getOwner().equals(owner) && p.getComment().equals(comment)) {
                // Product found
                return p;
            }
        }
        // No product found
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s, %s participants, owner %s, %s products", getName(), participants.size(), getOwner().getUsername(), getAmountOfProducts());
    }

    /**
     * Declare equality on same id
     *
     * @param obj other list
     * @return
     */
    @SuppressWarnings("JavaDoc")
    @Override
    public boolean equals(Object obj) {
        GroceryList other = (GroceryList) obj;
        return other.getId() == getId() || super.equals(obj);
    }

    /**
     * Updates a product
     *
     * @param productId
     * @param name
     * @param amount
     * @param comment
     * @param owner
     */
    @SuppressWarnings("JavaDoc")
    public void updateProduct(int productId, String name, int amount, String comment, Account owner) {
        for (Product p : productList) {
            if (p.getId() == productId) {
                p.setName(name);
                p.setAmount(amount);
                p.setComment(comment);
            }
        }
    }
}

