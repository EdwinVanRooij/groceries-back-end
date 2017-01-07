package me.evrooij.services;

import com.google.gson.Gson;
import me.evrooij.TestConfig;
import me.evrooij.data.Account;
import me.evrooij.data.GroceryList;
import me.evrooij.data.Product;
import me.evrooij.data.ResponseMessage;
import me.evrooij.managers.GroceryListManager;
import me.evrooij.util.DatabaseUtil;
import me.evrooij.util.DummyDataGenerator;
import me.evrooij.util.NetworkUtil;
import okhttp3.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author eddy on 5-1-17.
 */
public class GroceryListServiceTest {

    private DummyDataGenerator dummyDataGenerator;
    private Account thisAccount;
    private Account otherAccount;
    private GroceryList list;

    private GroceryListManager groceryListManager;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new DatabaseUtil().clean();
    }

    @Before
    public void setUp() throws Exception {
        groceryListManager = new GroceryListManager();
        dummyDataGenerator = new DummyDataGenerator();

        thisAccount = dummyDataGenerator.generateAccount();
        otherAccount = dummyDataGenerator.generateAccount();

        list = dummyDataGenerator.generateList(thisAccount);
    }

    @After
    public void tearDown() throws Exception {
        new DatabaseUtil().clean();
    }

    @Test
    public void newList() throws Exception {
        String listName = "NewList";

        new Thread(() -> {
            try {
                Response response = NetworkUtil.post(
                        "/lists/new",
                        "{\n" +
                                String.format("\"name\": \"%s\",\n", listName) +
                                "\"owner\": {\n" +
                                String.format("  \"id\": %s,\n", thisAccount.getId()) +
                                String.format("  \"username\": \"%s\",\n", thisAccount.getUsername()) +
                                String.format("  \"email\": \"%s\"\n", thisAccount.getEmail()) +
                                "  },\n" +
                                "    \"participants\": [{\n" +
                                String.format("    \"id\": %s,\n", otherAccount.getId()) +
                                String.format("    \"username\": \"%s\",\n", otherAccount.getUsername()) +
                                String.format("    \"email\": \"%s\"\n", otherAccount.getEmail()) +
                                "    }],\n" +
                                "\"productList\": []\n" +
                                "}"
                );

                // Verify
                GroceryList actual = new Gson().fromJson(response.body().string(), GroceryList.class);
                assertNotNull(actual);

                assertEquals(actual.getName(), listName);

                // Verify code
                int expectedCode = 200;
                int actualCode = response.code();
                assertEquals(expectedCode, actualCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(TestConfig.SLEEP_TIME);
    }

    @Test
    public void addParticipant() throws Exception {
        new Thread(() -> {
            try {
                Response response = NetworkUtil.post(
                        String.format("/lists/%s/participants/new", list.getId()),
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
                int expectedCode = 200;
                int actualCode = response.code();
                assertEquals(expectedCode, actualCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(TestConfig.SLEEP_TIME);
    }

    @Test
    public void deleteProduct() throws Exception {
        new Thread(() -> {
            try {
                Product product = groceryListManager.addProduct(list.getId(), dummyDataGenerator.generateProduct());

                Response response = NetworkUtil.delete(
                        String.format("/lists/%s/products/%s", list.getId(), product.getId())
                );

                // Verify message
                ResponseMessage expectedMessage = new ResponseMessage("Successfully deleted product.");
                ResponseMessage actualMessage = new Gson().fromJson(response.body().string(), ResponseMessage.class);
                assertEquals(expectedMessage, actualMessage);

                // Verify code
                int expectedCode = 200;
                int actualCode = response.code();
                assertEquals(expectedCode, actualCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(TestConfig.SLEEP_TIME);
    }

    @Test
    public void editProduct() throws Exception {
        Product product = groceryListManager.addProduct(list.getId(), dummyDataGenerator.generateProduct());
        String newName = "nameee";
        int newAmount = 9000;
        String newComment = "Anothaa!!";

        new Thread(() -> {
            try {
                Response response = NetworkUtil.put(
                        String.format("/lists/%s/products/%s/edit", list.getId(), product.getId()),
                        "{\n" +
                                String.format("\"id\": %s,\n", product.getId()) +
                                String.format("\"name\": \"%s\",\n", newName) +
                                String.format("\"amount\": %s,\n", newAmount) +
                                String.format("\"owner\": \"%s\",\n", product.getOwner()) +
                                String.format("\"comment\": \"%s\"\n", newComment) +
                                "}"
                );

                // Verify message
                ResponseMessage expectedMessage = new ResponseMessage("Product updated successfully.");
                ResponseMessage actualMessage = new Gson().fromJson(response.body().string(), ResponseMessage.class);
                assertEquals(expectedMessage, actualMessage);

                // Verify code
                int expectedCode = 200;
                int actualCode = response.code();
                assertEquals(expectedCode, actualCode);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(TestConfig.SLEEP_TIME);
    }

    @Test
    public void getList() throws Exception {
        new Thread(() -> {
            try {
                Response response = NetworkUtil.get(
                        String.format("/lists/%s", list.getId())
                );

                // Verify
                GroceryList actual = new Gson().fromJson(response.body().string(), GroceryList.class);
                assertEquals(list, actual);

                // Verify code
                int expectedCode = 200;
                int actualCode = response.code();
                assertEquals(expectedCode, actualCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(TestConfig.SLEEP_TIME);
    }

    @Test
    public void getLists() throws Exception {
        new Thread(() -> {
            try {
                Response response = NetworkUtil.get(
                        String.format("/user/%s", list.getId())
                );

                // Verify
                @SuppressWarnings("unchecked") List<GroceryList> list = new Gson().fromJson(response.body().string(), List.class);
                GroceryList actual = list.get(0);
                assertEquals(list, actual);

                // Verify code
                int expectedCode = 200;
                int actualCode = response.code();
                assertEquals(expectedCode, actualCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(TestConfig.SLEEP_TIME);
    }

}