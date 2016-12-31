package me.evrooij.util;

import spark.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author eddy on 31-12-16.
 */
public class NetworkUtil {
    public static String request(String method, String path) throws IOException {
        URL url = new URL("http://localhost:4567" + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.connect();
        return IOUtils.toString(connection.getInputStream());
    }
}
