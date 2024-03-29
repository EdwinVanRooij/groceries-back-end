package me.evrooij;

import me.evrooij.Config;
import okhttp3.*;

import java.io.IOException;

/**
 * @author eddy on 5-1-17.
 */
public class NetworkUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();

    public static Response post(String path, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(Config.getInstance().BASE_URL + path)
                .post(body)
                .build();
        return client.newCall(request).execute();
    }

    public static Response put(String path, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(Config.getInstance().BASE_URL + path)
                .put(body)
                .build();
        return client.newCall(request).execute();
    }

    public static Response delete(String path) throws IOException {
        Request request = new Request.Builder()
                .url(Config.getInstance().BASE_URL + path)
                .delete()
                .build();
        return client.newCall(request).execute();
    }

    public static Response get(String path) throws IOException {
        Request request = new Request.Builder()
                .url(Config.getInstance().BASE_URL + path)
                .build();
        return client.newCall(request).execute();
//        return response.body().string();
    }
}
