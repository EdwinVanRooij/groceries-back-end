package me.evrooij.util;

import me.evrooij.domain.Account;
import me.evrooij.domain.GroceryList;
import me.evrooij.domain.Product;
import me.evrooij.managers.AccountManager;
import me.evrooij.managers.GroceryListManager;

/**
 * @author eddy on 24-12-16.
 */
public class DummyDataGenerator {

    private static final String CORRECT_USERNAME_1 = "UsernameOne";
    private static final String CORRECT_USERNAME_2 = "UsernameTwo";
    private static final String CORRECT_USERNAME_3 = "UsernameThree";
    private static final String CORRECT_USERNAME_4 = "UserAfterThree";
    private static final String CORRECT_EMAIL_1 = "mailOne@gmail.com";
    private static final String CORRECT_EMAIL_2 = "mailTwo@gmail.com";
    private static final String CORRECT_EMAIL_3 = "mailThree@gmail.com";
    private static final String CORRECT_EMAIL_4 = "mailAfterThree@gmail.com";
    private static final String CORRECT_PASS_1 = "password";

    private static final String NAME_1 = "My List";

    private static final String PRODUCT_NAME_1 = "Apples1";
    private static final String PRODUCT_NAME_2 = "Apples2";
    private static final String PRODUCT_NAME_3 = "Apples3";
    private static final int PRODUCT_AMOUNT_1 = 3;
    private static final int PRODUCT_AMOUNT_2 = 2;
    private static final int PRODUCT_AMOUNT_3 = 5;
    private static final String PRODUCT_COMMENT_1 = "The red ones1";
    private static final String PRODUCT_COMMENT_2 = "The red ones2";
    private static final String PRODUCT_COMMENT_3 = "The red ones3";

    private AccountManager accountManager;
    private GroceryListManager listManager;

    public DummyDataGenerator() {
        System.out.println("Initializing...");
        accountManager = new AccountManager();
        listManager = new GroceryListManager();
    }

    public void generate() throws Exception {
        System.out.println("Generating dummy data...");

        Account account_1 = accountManager.registerAccount(CORRECT_USERNAME_1, CORRECT_EMAIL_1, CORRECT_PASS_1);
        Account account_2 = accountManager.registerAccount(CORRECT_USERNAME_2, CORRECT_EMAIL_2, CORRECT_PASS_1);
        Account account_3 = accountManager.registerAccount(CORRECT_USERNAME_3, CORRECT_EMAIL_3, CORRECT_PASS_1);
        Account account_4 = accountManager.registerAccount(CORRECT_USERNAME_4, CORRECT_EMAIL_4, CORRECT_PASS_1);
        System.out.println(String.format("Created account: %s", account_1.toString()));
        System.out.println(String.format("Created account: %s", account_2.toString()));
        System.out.println(String.format("Created account: %s", account_3.toString()));
        System.out.println(String.format("Created account: %s", account_4.toString()));

        GroceryList list = listManager.createGroceryList(NAME_1, account_1);
        System.out.println(String.format("Created list: %s", list.toString()));

        Product product_1 = listManager.addProduct(list.getId(), new Product(PRODUCT_NAME_1, PRODUCT_AMOUNT_1, PRODUCT_COMMENT_1, account_1.getUsername()));
        Product product_2 = listManager.addProduct(list.getId(), new Product(PRODUCT_NAME_2, PRODUCT_AMOUNT_2, PRODUCT_COMMENT_2, account_1.getUsername()));
        Product product_3 = listManager.addProduct(list.getId(), new Product(PRODUCT_NAME_3, PRODUCT_AMOUNT_3, PRODUCT_COMMENT_3, account_1.getUsername()));
        System.out.println(String.format("Created product: %s", product_1.toString()));
        System.out.println(String.format("Created product: %s", product_2.toString()));
        System.out.println(String.format("Created product: %s", product_3.toString()));
    }

    public static void main(String[] args) {
        try {
            new DummyDataGenerator().generate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
