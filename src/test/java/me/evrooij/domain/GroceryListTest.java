package me.evrooij.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author eddy on 15-12-16.
 */
public class GroceryListTest {

    private static final String USERNAME = "Hankinson";
    private static final String EMAIL = "Hankinson";
    private static final String PASSWORD = "Hankinson";
    private Account currentAccount;

    public static final String LIST_NAME = "MyList";
    private GroceryList currentList;

    @Before
    public void setUp() throws Exception {
        currentAccount = new Account(USERNAME, EMAIL, PASSWORD);
        currentList = new GroceryList(LIST_NAME, currentAccount);
    }

    @After
    public void tearDown() throws Exception {
        currentAccount = null;
        currentList = null;
    }

    @Test
    public void getName() throws Exception {
        String actual = currentList.getName();
        assertEquals(LIST_NAME, actual);
    }

    @Test
    public void getOwner() throws Exception {
        Account expected = currentAccount;
        Account actual = currentList.getOwner();
        /*
         * assertSame because the Account object does not override assertEquals, it would compare hashcodes
         * instead of object fields.
         */
        assertSame(expected, actual);
    }

    @Test
    public void addItem() throws Exception {
        /*
         * Declare some variables for the product
         */
        String productName = "Apples";
        int amount = 3;
        String comment = "Jonagold";
        String owner = currentAccount.getUsername();

        /*
         * Get the current size of the list to compare with later
         */
        int sizeBeforeAddition = currentList.getAmountOfProducts();
        /*
         * Add it to the list, so we can retrieve it later on
         */
        currentList.addItem(productName, amount, comment, owner);
        /*
         * Verify that we have one more product now
         */
        int expectedSize = sizeBeforeAddition + 1;
        assertEquals(expectedSize, currentList.getAmountOfProducts());
    }

    @Test
    public void editItem() throws Exception {
        /*
         * Declare some variables for the product
         */
        int id = 0;
        String productName = "Apples";
        int amount = 3;
        String comment = "Jonagold";
        String owner = currentAccount.getUsername();

        /*
         * Create the product to edit later on, add to the list as well
         */
        Product product = new Product(productName, amount, comment, owner);
        currentList.addItem(product.getName(), product.getAmount(), product.getComment(), product.getOwner());

        /*
         * Declare new values
         */
        String newProductName = "Big apples";
        int newAmount = 4;
        String newComment = "Jonagold in a basket";

        /*
         * Attempt to edit the item: happy flow
         */
        currentList.editItem(id, newProductName, newAmount, newComment, owner);
        Product actual = currentList.getProduct(id);

        // Check if name was changed
        assertEquals(newProductName, actual.getName());

        // Check if amount was changed
        assertEquals(newAmount, actual.getAmount());

        // Check if comment was changed
        assertEquals(newComment, actual.getComment());
    }

    @Test
    public void removeItem() throws Exception {
        int id = 10;

        // Add a product to the list
        currentList.addItem("Name", 10, "Comment", currentAccount.getUsername());

        // Remove the product
        currentList.removeItem(id);

        // Check whether the product was removed, should return null on no product found
        // Quote from getProduct(int id):
//        * @return the product if it's in here, null if it's not
        assertNull(currentList.getProduct(id));
    }

    @Test
    public void getAmountOfProducts() throws Exception {
        // Get the current amount of products, should be 0
        int expectedPreInsert = 0;
        int actualPreInsert = currentList.getAmountOfProducts();
        assertEquals(expectedPreInsert, actualPreInsert);

        // Add a product, expect 1
        int expectedPostInsert = 1;
        currentList.addItem("Name", 10, "Comment", currentAccount.getUsername());
        int actualPostInsert = currentList.getAmountOfProducts();
        assertEquals(expectedPostInsert, actualPostInsert);

        // Add 15 items, expect 16 now because we added one above
        for (int i = 0; i < 15; i++) {
            currentList.addItem("Name", 10, "Comment", currentAccount.getUsername());
        }
        int expectedPostLots = 16;
        int actualPostLots = currentList.getAmountOfProducts();
        assertEquals(expectedPostLots, actualPostLots);
    }


    @Test
    public void testConstructor() throws Exception {
        // Create a list
        GroceryList list = new GroceryList("Name", currentAccount);

        // Check if basic parameters were initialized correctly
        assertNotNull(list.getName());
        assertNotNull(list.getOwner());
    }
}