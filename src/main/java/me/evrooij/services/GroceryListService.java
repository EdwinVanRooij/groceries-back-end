package me.evrooij.services;

import com.google.gson.Gson;
import me.evrooij.domain.GroceryList;
import me.evrooij.domain.Product;
import me.evrooij.managers.GroceryListManager;
import me.evrooij.responses.ResponseMessage;

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

//       `/lists/<list_id>/products/<product_id>`
        delete("/lists/:listId/products/:productId", (request, response) -> {
            int listId = Integer.valueOf(request.params(":listId"));
            int productId = Integer.valueOf(request.params(":productId"));

            boolean result = listManager.deleteProduct(listId, productId);
            if (result) {
                return new ResponseMessage("Product deleted successfully.");
            } else {
                return new ResponseMessage("Error: product was not deleted.");
            }
        }, json());

        before(this::beforeRouteHandle);
        after(this::afterRouteHandle);
        exception(Exception.class, this::handleException);
    }
}
