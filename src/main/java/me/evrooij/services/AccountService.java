package me.evrooij.services;

import com.google.gson.Gson;
import me.evrooij.Config;
import me.evrooij.data.ResponseMessage;
import me.evrooij.util.JsonUtil;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import me.evrooij.data.Account;
import me.evrooij.data.Product;
import me.evrooij.data.ResponseMessage;
import me.evrooij.managers.AccountManager;
import me.evrooij.util.JsonUtil;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static me.evrooij.util.JsonUtil.json;
import static spark.Spark.*;

public class AccountService {
    private AccountManager accountManager;

    public AccountService() {
        accountManager = new AccountManager();

        get("/users/login", (request, response) -> {
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            return accountManager.login(username, password);
        }, json());

        post("/users/register", (request, response) -> {
            String json = request.body();
            System.out.println(String.format("Received json from /users/register in req body: %s", json));

            @SuppressWarnings("unchecked") Map<String, String> accountMap = new Gson().fromJson(request.body(), Map.class);
            String username = accountMap.get("username");
            String email = accountMap.get("email");
            String password = accountMap.get("password");
            System.out.println(String.format("Username: %s\nEmail: %s\nPassword: %s", username, email, password));

            Account returning = accountManager.registerAccount(username, email, password);
            System.out.println(String.format("Returning: %s", returning));
            return returning;
        }, json());

        get("/accounts/:accountId/friends/find", (request, response) -> {
            int accountId = Integer.valueOf(request.params(":accountId"));

            String searchQuery = request.queryParams("query");

            List<Account> accountList = accountManager.searchFriends(accountId, searchQuery);
            System.out.println(String.format("Returning a list of %s accounts in find account", accountList.size()));

            return accountList;
        }, json());

        get("/accounts/:accountId/friends", (request, response) -> {
            int accountId = Integer.valueOf(request.params(":accountId"));

            System.out.println(String.format("Searching for friends of account %s", accountId));
            List<Account> accountList = accountManager.getFriends(accountId);
            if (accountList != null) {
                System.out.println(String.format("returning an account list with %s accounts", accountList.size()));
            } else {
                System.out.println("Accountlist is null");
            }
            return accountList;
        }, json());

        post("/accounts/:accountId/friends/add", (request, response) -> {
            int accountId = Integer.valueOf(request.params(":accountId"));

            String json = request.body();
            System.out.println(String.format("Received json from /accounts/:accountId/friends/add in req body: %s", json));

            Account friend = new Gson().fromJson(request.body(), Account.class);
            System.out.println(String.format("Received new friend: %s", friend.toString()));

            boolean result = accountManager.addFriend(accountId, friend);
            if (result) {
                return new ResponseMessage("Successfully added friend.");
            } else {
                return new ResponseMessage("Could not add friend.");
            }
        }, json());

        post("/:accountId/myproducts/:productId", (request, response) -> {
            int accountId = Integer.valueOf(request.params(":accountId"));
            int productId = Integer.valueOf(request.params(":productId"));
            final String saveDir = getSavePath();
            final String fileName = String.valueOf(productId);

            MultipartConfigElement multipartConfigElement = new MultipartConfigElement(saveDir);
            request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

            for (Part p : request.raw().getParts()) {
                final Path path = Paths.get(String.format("%s/%s", saveDir, fileName));
                try (final InputStream in = p.getInputStream()) {
                    Files.copy(in, path);
                }
            }

            String ip = Config.getInstance().EXTERNAL_BASE_URL;
            String imageUrl = String.format("%s/products/%s/image", ip, productId);

            System.out.println(String.format("URL will be %s", imageUrl));

            accountManager.addProductImageUrl(accountId, productId, imageUrl);
            return new ResponseMessage(imageUrl);
        }, json());

        get("/products/:productId/image", (request, response) -> {
            int productId = Integer.valueOf(request.params(":productId"));

            final String saveDir = getSavePath();
            final String fileName = String.valueOf(productId);

            Path path = Paths.get(String.format("%s/%s", saveDir, fileName));
            byte[] data = Files.readAllBytes(path);

            HttpServletResponse raw = response.raw();

            response.header("Content-Disposition", String.format("attachment; filename=%s", fileName));
            response.type("application/force-download");

            raw.getOutputStream().write(data);
            raw.getOutputStream().flush();
            raw.getOutputStream().close();

            return raw;
        });

        get("/:accountId/myproducts", (request, response) -> {
            int accountId = Integer.valueOf(request.params(":accountId"));

            return accountManager.getAllMyProducts(accountId);
        }, json());

        post("/:accountId/myproducts", (request, response) -> {
            int accountId = Integer.valueOf(request.params(":accountId"));

            Product product = new Gson().fromJson(request.body(), Product.class);
            System.out.println(String.format("Retrieved product: %s", product.toString()));

            return accountManager.addToMyProducts(accountId, product);
        }, json());

        after((request, response) -> response.type("application/json"));

        exception(Exception.class, (exception, request, response) -> {
            response.status(HTTP_BAD_REQUEST);
            response.type("application/json");
            response.body(JsonUtil.toJson(new ResponseMessage(exception)));
        });
    }

    @SuppressWarnings("Duplicates")
    private String getSavePath() {
        String path = "./config.properties";
        try (FileInputStream fis = new FileInputStream(path);
             BufferedReader in = new BufferedReader(new InputStreamReader(fis))) {

            Properties prop = new Properties();
            prop.load(in);
            return prop.getProperty("path");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
