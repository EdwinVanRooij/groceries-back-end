package me.evrooij.managers;

import me.evrooij.data.Account;
import me.evrooij.data.GroceryList;
import me.evrooij.data.Product;
import me.evrooij.exceptions.InvalidParticipantException;
import me.evrooij.util.DatabaseUtil;
import me.evrooij.util.DummyDataGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author eddy on 23-12-16.
 */
public class GroceryListManagerTest {

    private static final String NAME_1 = "My List";
    private static final String NAME_2 = "This is another list";
    private static final String NAME_3 = "List";

    private Account thisAccount;
    private GroceryListManager groceryListManager;
    private DummyDataGenerator dummyDataGenerator;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Before
    public void setUp() throws Exception {
        groceryListManager = new GroceryListManager();
        dummyDataGenerator = new DummyDataGenerator();

        thisAccount = dummyDataGenerator.generateAccount();
    }

    @After
    public void tearDown() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void createGroceryListWithInitialParticipants() throws Exception {
        /*
         * Check if no participants are added on 0 length participants addition
         */
        List<Account> participants_1 = new ArrayList<>();
        GroceryList list_1 = groceryListManager.createGroceryList(NAME_1, thisAccount, participants_1);
        int expectedSize_1 = 0;
        assertEquals(expectedSize_1, list_1.getAmountOfParticipants());
        /*
         * Check if all participants are added on initial participants addition
         */
        participants_1.add(dummyDataGenerator.generateAccount());
        participants_1.add(dummyDataGenerator.generateAccount());
        GroceryList list_2 = groceryListManager.createGroceryList(NAME_2, thisAccount, participants_1);
        int expectedSize_2 = 2;
        assertEquals(expectedSize_2, list_2.getAmountOfParticipants());
    }

    @Test
    public void createGroceryList() throws Exception {
        /*
         * Create a list, verify existence by retrieving it
         */
        GroceryList list_1 = groceryListManager.createGroceryList(NAME_1, thisAccount, null);
        assertNotNull(list_1);
        // Check if getting this list is the same as
        // the just created list.
        GroceryList list_2 = groceryListManager.getList(list_1.getId());
        assertEquals(list_1, list_2);

        // Robustness checks
        GroceryList list_3 = groceryListManager.createGroceryList(NAME_2, thisAccount, null);
        assertNotNull(list_3);
        GroceryList list_4 = groceryListManager.createGroceryList(NAME_3, thisAccount, null);
        assertNotNull(list_4);
    }

    @Test
    public void getListsByAccountId() throws Exception {
        /*
         * Get current lists by account id, should be 0
         */
        int expected = 0;
        int actual = groceryListManager.getListsByAccountId(thisAccount.getId()).size();
        assertEquals(expected, actual);

        // Create a list
        GroceryList list_1 = groceryListManager.createGroceryList(NAME_1, thisAccount, null);
        // Verify the one we get by account id is the one we just created
        assertEquals(list_1, groceryListManager.getListsByAccountId(thisAccount.getId()).get(0));

        // Create another list
        groceryListManager.createGroceryList(NAME_2, thisAccount, null);
        // Verify we have 2 lists now
        int expected_2 = 2;
        int actual_2 = groceryListManager.getListsByAccountId(thisAccount.getId()).size();
        assertEquals(expected_2, actual_2);
    }

    @Test
    public void getList() throws Exception {
        GroceryList expected = groceryListManager.createGroceryList(NAME_1, thisAccount, null);
        GroceryList actual = groceryListManager.getList(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    public void addProduct() throws Exception {
        /*
         * Verify a product is added to the list if the list exists
         */
        GroceryList list = groceryListManager.createGroceryList(NAME_1, thisAccount, null);
        String name = "Apples";
        int amount = 3;
        String comment = "The red ones";
        Product product = new Product(name, amount, comment, thisAccount);
        Product productFromList = groceryListManager.addProduct(list.getId(), product);
        assertNotNull(productFromList);

        /*
         * Verify a product is not added to the list if the list doesn't exist
         */
        try {
            groceryListManager.addProduct(-1, product);
            fail();
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void deleteProduct() throws Exception {
        /*
         * Verify an existent product is deleted after deleteProduct
         */
        GroceryList list = groceryListManager.createGroceryList(NAME_1, thisAccount, null);
        Product product = new Product("Apples", 3, "The red ones", thisAccount);
        Product productFromList = groceryListManager.addProduct(list.getId(), product);
        boolean result = groceryListManager.deleteProduct(list.getId(), productFromList.getId());
        assertTrue(result);
        /*
         * Verify the result is false if the product to delete doesn't exist
         */
        try {
            groceryListManager.deleteProduct(list.getId(), productFromList.getId());
            fail();
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void addParticipant() throws Exception {
        /*
         * Check if participant was added to list with good listId
         */
        GroceryList list = groceryListManager.createGroceryList(NAME_1, thisAccount, null);
        Account newParticipant = dummyDataGenerator.generateAccount();
        boolean result_1 = groceryListManager.addParticipant(list.getId(), newParticipant);
        // Verify if this worked at all
        assertTrue(result_1);
        // Get list from manager just to be sure
        GroceryList listFromManager = groceryListManager.getList(list.getId());
        assertEquals(newParticipant, listFromManager.getParticipants().get(0));

        /*
         * Check if owner is not being added to the new list
         */
        try {
            groceryListManager.addParticipant(list.getId(), thisAccount);
            fail();
        } catch (InvalidParticipantException e) {
        }

        /*
         * Check if we can't add newParticipant again
         */
        try {
            groceryListManager.addParticipant(list.getId(), newParticipant);
            fail();
        } catch (InvalidParticipantException e) {
        }
    }

    @Test
    public void updateProduct() throws Exception {
        // Create some valid objects
        GroceryList list = groceryListManager.createGroceryList(NAME_1, thisAccount, null);
        String name = "apples";
        int amount = 10;
        String comment = "red ones";
        String owner = "foobar";
        list.addProduct(name, amount, comment, thisAccount);
        Product product = list.getProduct(name, thisAccount, comment);
        /*
         * Check if product isn't updated on invalid listId
         */
        try {
            groceryListManager.updateProduct(list.getId() - 1, product.getId(), product);
            fail();
        } catch (NullPointerException e) {
        }
        /*
         * Check if product isn't updated on invalid productId
         */
        try {
            groceryListManager.updateProduct(list.getId(), product.getId() - 1, product);
        } catch (NullPointerException e) {
        }
        /*
         * Check if product is updated on valid listId and productId
         */
        // Declare some new fields
        String newName = "pears";
        int newAmount = 11;
        String newComment = "purple ones";

        // Update and verify fields
        product.setName(newName);
        product.setAmount(newAmount);
        product.setComment(newComment);

        groceryListManager.updateProduct(list.getId(), product.getId(), product);
        Product productFromManager = groceryListManager.getList(list.getId()).getProduct(product.getId());

        String actualName = productFromManager.getName();
        assertEquals(newName, actualName);

        int actualAmount = productFromManager.getAmount();
        assertEquals(newAmount, actualAmount);

        String actualComment = productFromManager.getComment();
        assertEquals(newComment, actualComment);
    }
}






