package me.evrooij;

/**
 * @author eddy on 5-1-17.
 */
public class Config {
    // Development
    public static final int PORT = 4567;
    // Production
//    public static final int PORT = 6438;

    public static final String BASE_URL = String.format("http://127.0.0.1:%d", PORT);

    // Account service
    public static final String PATH_LOGIN = "/users/login";
    public static final String PATH_REGISTER = "/users/register";
    public static final String PATH_FIND_FRIEND = "/accounts/:accountId/friends/find";
    public static final String PATH_GET_FRIENDS = "/accounts/:accountId/friends";
    public static final String PATH_ADD_FRIEND = "/accounts/:accountId/friends/add";

    // Feedback service
    public static final String PATH_FEEDBACK_NEW = "/feedback/new";

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

}