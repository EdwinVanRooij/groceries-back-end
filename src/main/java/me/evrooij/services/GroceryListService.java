package me.evrooij.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.evrooij.data.Account;
import me.evrooij.data.GroceryList;
import me.evrooij.data.Product;
import me.evrooij.data.ResponseMessage;
import me.evrooij.managers.GroceryListManager;
import me.evrooij.util.JsonUtil;

import static me.evrooij.Config.*;
import static me.evrooij.util.JsonUtil.json;
import static spark.Spark.*;

public class GroceryListService {
    private GroceryListManager listManager;

    public GroceryListService() {
        listManager = new GroceryListManager();

        get("/user/:id/lists", (request, response) -> {
            String idString = request.params(":id");
            int id = Integer.valueOf(idString);

            return listManager.getListsByAccountId(id);
        }, json());

        get("/lists/:id", (request, response) -> {
            int listId = Integer.valueOf(request.params(":id"));

            return listManager.getList(listId);
        }, json());

        post("/lists/new", (request, response) -> {
            String json = request.body();
            System.out.println(String.format("Received json from /lists/new in req body: %s", json));

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            GroceryList list = gson.fromJson(json, GroceryList.class);
            System.out.println(String.format("Retrieved list: %s", list.toString()));

            GroceryList listFromDb = listManager.createGroceryList(list.getName(), list.getOwner(), list.getParticipants());
            System.out.println(String.format("Returning list: %s", listFromDb.toString()));

            return listFromDb;
        }, json());

        put("/lists/:listId/products/:productId/edit", (request, response) -> {
            int listId = Integer.valueOf(request.params(":listId"));
            int productId = Integer.valueOf(request.params(":productId"));
            String json = request.body();
            System.out.println(String.format("Received json from /lists/%s/products/%s/edit in req body: %s", listId, productId, json));

            Product product = new Gson().fromJson(json, Product.class);
            System.out.println(String.format("Retrieved product: %s", product.toString()));

            boolean result = listManager.updateProduct(listId, productId, product);
            if (result) {
                return new ResponseMessage("Product updated successfully.");
            } else {
                return new ResponseMessage("Could not update product.");
            }
        }, json());

        post("/lists/:listId/participants/new", (request, response) -> {
            int listId = Integer.valueOf(request.params(":listId"));

            String json = request.body();
            System.out.println(String.format("Received json from /lists/%s/participants/new in req body: %s", listId, json));

            Account newParticipant = new Gson().fromJson(json, Account.class);
            System.out.println(String.format("Retrieved new participant: %s", newParticipant.toString()));

            if (listManager.addParticipant(listId, newParticipant)) {
                return new ResponseMessage("Successfully added new participant.");
            } else {
                return new ResponseMessage("Error: participant was not added.");
            }
        }, json());

        post("/list/:id/products/new", (request, response) -> {
            String idString = request.params(":id");
            int id = Integer.valueOf(idString);

            String json = request.body();
            System.out.println(String.format("Received json from /list/%s/products/new in req body: %s", String.valueOf(id), json));

            Product product = new Gson().fromJson(json, Product.class);
            System.out.println(String.format("Retrieved product: %s", product.toString()));

            Product productFromDb = listManager.addProduct(id, product);
            System.out.println(String.format("Returning product %s", productFromDb.toString()));

            return productFromDb;

        }, json());

        delete("/lists/:listId/products/:productId", (request, response) -> {
            int listId = Integer.valueOf(request.params(":listId"));
            int productId = Integer.valueOf(request.params(":productId"));

            if (listManager.deleteProduct(listId, productId)) {
                return new ResponseMessage("Successfully deleted product.");
            } else {
                return new ResponseMessage("Error: product was not deleted.");
            }
        }, json());

        after((request, response) -> response.type("application/json"));

        exception(Exception.class, (exception, request, response) -> {
            response.status(400);
            response.type("application/json");
            response.body(JsonUtil.toJson(new ResponseMessage(exception)));
        });
    }
}
