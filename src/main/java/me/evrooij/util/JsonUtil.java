package me.evrooij.util;

import com.google.gson.Gson;
import me.evrooij.domain.Account;
import spark.ResponseTransformer;

public class JsonUtil {

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static ResponseTransformer json() {
        return new ResponseTransformer() {
            @Override
            public String render(Object model) throws Exception {
                return toJson(model);
            }
        };
    }
}
