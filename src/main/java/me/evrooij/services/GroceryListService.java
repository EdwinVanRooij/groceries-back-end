package me.evrooij.services;

import com.google.gson.Gson;
import me.evrooij.domain.GroceryList;
import me.evrooij.managers.GroceryListManager;

import static me.evrooij.util.JsonUtil.json;
import static spark.Spark.*;

public class GroceryListService extends DefaultService {
    private GroceryListManager listManager;

    public GroceryListService() {
        listManager = new GroceryListManager();

        get("/user/:id/lists", (request, response) -> {
            String idString = request.params(":id");
            int id = Integer.valueOf(idString);

            return listManager.getListsByAccountId(id);
        }, json());

        post("/lists/new", (request, response) -> {
            String json = request.body();
            System.out.println(String.format("Received json from /lists/new in req body: %s", json));

            GroceryList list = new Gson().fromJson(json, GroceryList.class);
            System.out.println(String.format("Retrieved list: %s", list.toString()));

            GroceryList listFromDb = listManager.createGroceryList(list.getName(), list.getOwner());
            System.out.println(String.format("Returning list: %s", listFromDb.toString()));

            return listFromDb;
        }, json());

        before(this::beforeRouteHandle);
        after(this::afterRouteHandle);
        exception(Exception.class, this::handleException);
    }
}
