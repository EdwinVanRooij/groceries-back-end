package me.evrooij;

import me.evrooij.services.AccountService;
import me.evrooij.services.DummyService;
import me.evrooij.services.FeedbackService;
import me.evrooij.services.GroceryListService;

import static spark.Spark.port;

public class Main {
    public static void main(String[] args) {
        // Production env
        port(Config.PORT);

        // Source: https://dzone.com/articles/building-simple-restful-api
        new AccountService();
        new GroceryListService();
        new FeedbackService();
        new DummyService();
    }
}
