package me.evrooij;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author eddy on 5-1-17.
 */
public class Config {
    // Development
    public int PORT;
//    public static final int PORT = 6438;

    public String BASE_URL;

    // Account service
    public static final String PATH_LOGIN = "/users/login";
    public static final String PATH_REGISTER = "/users/register";
    public static final String PATH_FIND_FRIEND = "/accounts/:accountId/friends/find";
    public static final String PATH_GET_FRIENDS = "/accounts/:accountId/friends";
    public static final String PATH_ADD_FRIEND = "/accounts/:accountId/friends/add";

    // Feedback service
    public static final String PATH_FEEDBACK_NEW = "/feedback/new";
    public static final String PATH_FEEDBACK_DELETE = "/feedback/:id";
    public static final String PATH_FEEDBACK_LIST = "/feedback";

    // GroceryList service
    // Lists
    public static final String PATH_LISTS = "/user/:id/lists";
    public static final String PATH_LISTS_GET = "/lists/:id";
    public static final String PATH_LISTS_NEW = "/lists/new";

    // Participants of a list
    public static final String PATH_LISTS_PARTICIPANTS_NEW = "/lists/:listId/participants/new";

    // Products of a list
    public static final String PATH_LISTS_PRODUCTS_NEW = "/list/:id/products/new";
    public static final String PATH_LISTS_PRODUCTS_EDIT = "/lists/:listId/products/:productId/edit";
    public static final String PATH_LISTS_PRODUCTS_DELETE = "/lists/:listId/products/:productId";

    private static Config ourInstance = new Config();

    public static Config getInstance() {
        return ourInstance;
    }

    private Config() {
        try {
            String path = "./config.properties";
            FileInputStream fis = new FileInputStream(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Properties prop = new Properties();
            prop.load(in);
            in.close();

            PORT = Integer.valueOf(prop.getProperty("port"));
            BASE_URL = String.format("http://127.0.0.1:%d", PORT);
            System.out.println(String.format("Found port %s", String.valueOf(PORT)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
