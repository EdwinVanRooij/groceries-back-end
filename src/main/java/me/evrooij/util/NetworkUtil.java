package me.evrooij.util;

import me.evrooij.Config;
import okhttp3.*;

import java.io.IOException;

/**
 * @author eddy on 5-1-17.
 */
public class NetworkUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();

    public static String post(String path, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(Config.BASE_URL + path)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String put(String path, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(Config.BASE_URL + path)
                .put(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String delete(String path) throws IOException {
        Request request = new Request.Builder()
                .url(Config.BASE_URL + path)
                .delete()
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public static String get(String path) throws IOException {
        Request request = new Request.Builder()
                .url(Config.BASE_URL + path)
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

}
