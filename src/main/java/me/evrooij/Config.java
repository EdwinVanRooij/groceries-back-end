package me.evrooij;

/**
 * @author eddy on 5-1-17.
 */
public class Config {
    // Development
    public static final int PORT = 4567;
    // Production
//    public static final int PORT = 6438;

    public static final String URL = String.format("http://127.0.0.1:%d", PORT);
}
