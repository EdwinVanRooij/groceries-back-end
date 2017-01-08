package me.evrooij.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.evrooij.data.Account;
import me.evrooij.data.GroceryList;
import me.evrooij.data.Product;
import me.evrooij.data.ResponseMessage;
import me.evrooij.util.DatabaseUtil;
import me.evrooij.util.DummyDataGenerator;
import me.evrooij.util.NetworkUtil;
import okhttp3.Response;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author eddy on 5-1-17.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GroceryListServiceTest {

    private static Account thisAccount;
    private static Account otherAccount;

    private static GroceryList createdList;
    private static Product createdProduct;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new DatabaseUtil().clean();
        DummyDataGenerator dummyDataGenerator = new DummyDataGenerator();

        thisAccount = dummyDataGenerator.generateAccount();
        otherAccount = dummyDataGenerator.generateAccount();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void test7_newProduct() throws Exception {
        String name = "nameee";
        int amount = 10;
        String comment = "Anothaa!!";

        Response response = NetworkUtil.post(
                String.format("/list/%s/products/new", createdList.getId()),
                "{\n" +
                        String.format("\"name\": \"%s\",\n", name) +
                        String.format("\"amount\": %s,\n", amount) +
                        String.format("\"owner\": \"%s\",\n", thisAccount.getUsername()) +
                        String.format("\"comment\": \"%s\"\n", comment) +
                        "}"
        );

        // Verify message
        Product actual = new Gson().fromJson(response.body().string(), Product.class);
        assertNotNull(actual);

        // Sample
        assertEquals(name, actual.getName());

        // Set product for tests after this one
        createdProduct = actual;

        // Verify code
        assertEquals(HTTP_OK, response.code());
    }

    @Test
    public void test1_newList() throws Exception {
        String listName = "NewList";

        Response response = NetworkUtil.post(
                "/lists/new",
                "{\n" +
                        String.format("\"name\": \"%s\",\n", listName) +
                        "\"owner\": {\n" +
                        String.format("  \"id\": %s,\n", thisAccount.getId()) +
                        String.format("  \"username\": \"%s\",\n", thisAccount.getUsername()) +
                        String.format("  \"email\": \"%s\"\n", thisAccount.getEmail()) +
                        "  },\n" +
                        "    \"participants\": [],\n" +
                        "\"productList\": []\n" +
                        "}"
        );

        // Verify
        GroceryList actual = new Gson().fromJson(response.body().string(), GroceryList.class);
        assertNotNull(actual);

        assertEquals(actual.getName(), listName);

        // Set list for tests after this one
        createdList = actual;

        // Verify code
        assertEquals(HTTP_OK, response.code());
    }

    @Test
    public void test2_addParticipant() throws Exception {
        Response response = NetworkUtil.post(
                String.format("/lists/%s/participants/new", createdList.getId()),
                "{\n" +
                        String.format("\"id\": %s,\n", otherAccount.getId()) +
                        String.format("\"username\": \"%s\",\n", otherAccount.getUsername()) +
                        String.format("\"email\": \"%s\"\n", otherAccount.getEmail()) +
                        "}"
        );

        // Verify message
        ResponseMessage expectedMessage = new ResponseMessage("Successfully added new participant.");
        ResponseMessage actualMessage = new Gson().fromJson(response.body().string(), ResponseMessage.class);
        assertEquals(expectedMessage, actualMessage);

        // Verify code
        assertEquals(HTTP_OK, response.code());
    }

    @Test
    public void test9_deleteProduct() throws Exception {
        Response response = NetworkUtil.delete(
                String.format("/lists/%s/products/%s", createdList.getId(), createdProduct.getId())
        );

        // Verify message
        ResponseMessage expectedMessage = new ResponseMessage("Successfully deleted product.");
        ResponseMessage actualMessage = new Gson().fromJson(response.body().string(), ResponseMessage.class);
        assertEquals(expectedMessage, actualMessage);

        // Verify code
        assertEquals(HTTP_OK, response.code());
    }

    @Test
    public void test8_editProduct() throws Exception {
        String newName = "nameee";
        int newAmount = 9000;
        String newComment = "Anothaa!!";

        Response response = NetworkUtil.put(
                String.format("/lists/%s/products/%s/edit", createdList.getId(), createdProduct.getId()),
                "{\n" +
                        String.format("\"id\": %s,\n", createdProduct.getId()) +
                        String.format("\"name\": \"%s\",\n", newName) +
                        String.format("\"amount\": %s,\n", newAmount) +
                        String.format("\"owner\": \"%s\",\n", createdProduct.getOwner()) +
                        String.format("\"comment\": \"%s\"\n", newComment) +
                        "}"
        );

        // Verify message
        ResponseMessage expectedMessage = new ResponseMessage("Product updated successfully.");
        ResponseMessage actualMessage = new Gson().fromJson(response.body().string(), ResponseMessage.class);
        assertEquals(expectedMessage, actualMessage);

        // Verify code
        assertEquals(HTTP_OK, response.code());
    }

    @Test
    public void test3_getList() throws Exception {
        Response response = NetworkUtil.get(
                String.format("/lists/%s", createdList.getId())
        );

        // Verify
        GroceryList actual = new Gson().fromJson(response.body().string(), GroceryList.class);
        assertEquals(createdList, actual);

        // Verify code
        assertEquals(HTTP_OK, response.code());
    }

    @Test
    public void test4_getLists() throws Exception {
        Response response = NetworkUtil.get(
                String.format("/user/%s/lists", thisAccount.getId())
        );

        // Verify
        Type listType = new TypeToken<ArrayList<GroceryList>>() {
        }.getType();
        List<GroceryList> list = new Gson().fromJson(response.body().string(), listType);
        GroceryList actual = list.get(0);
        assertEquals(createdList, actual);


        // Verify code
        assertEquals(HTTP_OK, response.code());
    }
}