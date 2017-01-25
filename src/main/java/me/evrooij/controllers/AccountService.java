package me.evrooij.controllers;

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
import me.evrooij.managers.AccountManager;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static me.evrooij.util.JsonUtil.json;
import static spark.Spark.*;

public class AccountService {
    private AccountManager accountManager;

    public AccountService() {
        accountManager = new AccountManager();


        post("/", (request, response) -> {
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
