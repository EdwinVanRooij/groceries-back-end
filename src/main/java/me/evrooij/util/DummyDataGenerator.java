package me.evrooij.util;

import me.evrooij.data.Account;
import me.evrooij.data.GroceryList;
import me.evrooij.data.Product;
import me.evrooij.managers.AccountManager;
import me.evrooij.managers.GroceryListManager;

/**
 * @author eddy on 24-12-16.
 */
public class DummyDataGenerator {

    private static final String[] CORRECT_USERNAMES = new String[]{"UsernameOne", "UsernameTwo", "UsernameThree", "UserAfterThree"};
    private static final String[] CORRECT_EMAILS = new String[]{"mailOne@gmail.com", "mailTwo@gmail.com", "mailThree@gmail.com", "mailAfterThree@gmail.com"};
    private static final String CORRECT_PASS = "password";

    private static final String LIST_NAME = "My List";

    private static final String PRODUCT_NAME = "Apples1";
    private static final int PRODUCT_AMOUNT = 3;
    private static final String PRODUCT_COMMENT = "The red ones1";

    private int accountIndex = -1;

    private AccountManager accountManager;
    private GroceryListManager listManager;

    public DummyDataGenerator() {
        System.out.println("Initializing...");
        accountManager = new AccountManager();
        listManager = new GroceryListManager();
    }

    public Account generateAccount() {
        accountIndex++;
        return accountManager.registerAccount(CORRECT_USERNAMES[accountIndex], CORRECT_EMAILS[accountIndex], CORRECT_PASS);
    }

    public void generate() throws Exception {
        System.out.println("Generating dummy data...");

        Account account_1 = generateAccount();
        Account account_2 = generateAccount();
        Account account_3 = generateAccount();
        generateAccount();

        accountManager.addFriend(account_1.getId(), account_2);
        accountManager.addFriend(account_1.getId(), account_3);

        GroceryList list = listManager.createGroceryList(LIST_NAME, account_1, null);

        listManager.addParticipant(list.getId(), account_2);
        listManager.addParticipant(list.getId(), account_3);

        for (int i = 0; i < 10; i++) {
            listManager.addProduct(list.getId(), new Product(String.format("%s: %s", PRODUCT_NAME, i), PRODUCT_AMOUNT, PRODUCT_COMMENT, account_1.getUsername()));
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
