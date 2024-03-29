package me.evrooij.util;

import me.evrooij.data.Account;
import me.evrooij.data.Feedback;
import me.evrooij.data.GroceryList;
import me.evrooij.data.Product;
import me.evrooij.exceptions.DuplicateUsernameException;
import me.evrooij.managers.AccountManager;
import me.evrooij.managers.FeedbackManager;
import me.evrooij.managers.GroceryListManager;

/**
 * @author eddy on 24-12-16.
 */
public class DummyDataGenerator {

    private static final String[] CORRECT_USERNAMES = new String[]{"UsernameOne", "UsernameTwo", "UsernameThree", "UserAfterThree"};
    private static final String[] CORRECT_EMAILS = new String[]{"mailOne@gmail.com", "mailTwo@gmail.com", "mailThree@gmail.com", "mailAfterThree@gmail.com"};
    private static final String CORRECT_PASS = "password";

    private static final String[] GROCERY_LIST_NAMES = new String[]{"MyList 1", "MyList 2", "MyList 3", "MyList 4"};

    private static final String[] FEEDBACK_MESSAGES = new String[]{"Help me with this!", "Another bug!!", "So many bugs!!", "Please implement this thingy.."};

    private static final String PRODUCT_NAME = "Apples1";
    private static final int PRODUCT_AMOUNT = 3;
    private static final String PRODUCT_COMMENT = "The red ones1";

    private int feedbackIndex = -1;
    private int productIndex = -1;
    private int groceryListIndex = -1;
    private int accountIndex = -1;

    private AccountManager accountManager;
    private GroceryListManager listManager;
    private FeedbackManager feedbackManager;

    public DummyDataGenerator() {
        accountManager = new AccountManager();
        listManager = new GroceryListManager();
        feedbackManager = new FeedbackManager();
    }

    /**
     * Generates a new feedback item
     */
    public Feedback generateFeedback(Account sender) {
        feedbackIndex++;
        return feedbackManager.reportFeedback(FEEDBACK_MESSAGES[feedbackIndex], Feedback.Type.Bug, sender);
    }

    /**
     * Generates a new product
     */
    public Product generateProduct(GroceryList list, Account owner) {
        productIndex++;
        return listManager.addProduct(list.getId(), new Product(String.format("%s - %s", PRODUCT_NAME, productIndex), PRODUCT_AMOUNT, PRODUCT_COMMENT, owner));
    }

    public Product generateProductNoId(Account owner) {
        productIndex++;
        return new Product(String.format("%s - %s", PRODUCT_NAME, productIndex), PRODUCT_AMOUNT, PRODUCT_COMMENT, owner);
    }

    /**
     * Generates a new GroceryList
     *
     * @param owner owner of the list
     * @return a new GroceryList
     */
    public GroceryList generateList(Account owner) {
        groceryListIndex++;
        return listManager.createGroceryList(GROCERY_LIST_NAMES[groceryListIndex], owner, null);
    }

    /**
     * Generates a new Account, max 4 accounts
     */
    public Account generateAccount() throws DuplicateUsernameException {
        accountIndex++;
        return accountManager.registerAccount(CORRECT_USERNAMES[accountIndex], CORRECT_EMAILS[accountIndex], CORRECT_PASS);
    }

    public void generate() throws Exception {
        Account account_1 = generateAccount();
        Account account_2 = generateAccount();
        Account account_3 = generateAccount();
        generateAccount();

        accountManager.addFriend(account_1.getId(), account_2);
        accountManager.addFriend(account_1.getId(), account_3);

        GroceryList list = generateList(account_1);

        listManager.addParticipant(list.getId(), account_2);
        listManager.addParticipant(list.getId(), account_3);

        for (int i = 0; i < 10; i++) {
            listManager.addProduct(list.getId(), new Product(String.format("%s: %s", PRODUCT_NAME, i), PRODUCT_AMOUNT, PRODUCT_COMMENT, account_1));
        }
    }

    public static void main(String[] args) {
        try {
            new DummyDataGenerator().generate();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
