package me.evrooij.util;

import spark.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author eddy on 31-12-16.
 */
public class NetworkUtil {
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    public static String request(String method, String path) throws IOException {
        URL url = new URL("http://localhost:4567" + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.connect();
        return IOUtils.toString(connection.getInputStream());
    }

    // Look on the internet for a library
    // withURL
    // withMethod
    // .execute()
}
