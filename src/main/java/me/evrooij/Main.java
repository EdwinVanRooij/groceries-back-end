package me.evrooij;

import me.evrooij.services.AccountService;
import me.evrooij.services.GroceryListService;

public class Main {
    public static void main(String[] args) {
        // Source: https://dzone.com/articles/building-simple-restful-api
//        new UserService();
        new AccountService();
        new GroceryListService();
    }
}
