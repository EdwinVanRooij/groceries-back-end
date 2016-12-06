package me.evrooij.two;

public class Main {
    public static void main(String[] args) {
        // Source: https://dzone.com/articles/building-simple-restful-api
        new UserController(new UserService());
    }
}
