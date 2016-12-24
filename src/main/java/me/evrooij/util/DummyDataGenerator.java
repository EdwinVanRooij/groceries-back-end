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
    private static final String CORRECT_EMAIL_1 = "mail@gmail.com";
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

        Account account = accountManager.registerAccount(CORRECT_USERNAME_1, CORRECT_EMAIL_1, CORRECT_PASS_1);
        System.out.println(String.format("Created account: %s", account.toString()));

        GroceryList list = listManager.createGroceryList(NAME_1, account);
        System.out.println(String.format("Created list: %s", list.toString()));

        Product product_1 = listManager.addProduct(list.getId(), new Product(PRODUCT_NAME_1, PRODUCT_AMOUNT_1, PRODUCT_COMMENT_1, account.getUsername()));
        Product product_2 = listManager.addProduct(list.getId(), new Product(PRODUCT_NAME_2, PRODUCT_AMOUNT_2, PRODUCT_COMMENT_2, account.getUsername()));
        Product product_3 = listManager.addProduct(list.getId(), new Product(PRODUCT_NAME_3, PRODUCT_AMOUNT_3, PRODUCT_COMMENT_3, account.getUsername()));
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
