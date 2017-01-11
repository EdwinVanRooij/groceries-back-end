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
