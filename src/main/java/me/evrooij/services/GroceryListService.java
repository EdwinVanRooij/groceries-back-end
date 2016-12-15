package me.evrooij.services;

import me.evrooij.managers.GroceryListManager;

import static me.evrooij.util.JsonUtil.json;
import static spark.Spark.*;

public class GroceryListService extends DefaultService {
    private GroceryListManager listManager;

    public GroceryListService() {
        listManager = new GroceryListManager();

        get("/users/:id/login", (request, response) -> {
            String idString = request.params(":id");
            int id = Integer.valueOf(idString);

            return listManager.getLists(id);
        }, json());

        before(this::beforeRouteHandle);
        after(this::afterRouteHandle);
        exception(Exception.class, this::handleException);
    }
}
