package me.evrooij.services;

import me.evrooij.data.Student;
import me.evrooij.managers.AccountManager;

import static me.evrooij.util.JsonUtil.json;
import static spark.Spark.*;

public class DummyService {
    private AccountManager accountManager;

    public DummyService() {
        accountManager = new AccountManager();

        // Production env
//        port(6438);

        get("/student", (request, response) -> {
            System.out.println("Getting request from dummy service...");

            String name = request.queryParams("name");
            System.out.println("Name: %s");

            int age = Integer.valueOf(request.queryParams("age"));
            System.out.println(String.format("Age: %s", String.valueOf(age)));

            return new Student(name, age);
        }, json());
    }
}
