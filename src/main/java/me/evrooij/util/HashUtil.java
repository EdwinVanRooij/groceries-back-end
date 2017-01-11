package me.evrooij.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

/**
 * @author eddy on 11-1-17.
 */
public class HashUtil {

    public static String hash(String message) {
        // Create instance
        Argon2 argon2 = Argon2Factory.create();

        // Hash message
        int N = 65536;
        int r = 2;
        int p = 1;
        return argon2.hash(r, N, p, message);
    }

    public static boolean equals(String hash, String message) {
        // Create instance
        Argon2 argon2 = Argon2Factory.create();
        char[] messageCharArray = message.toCharArray();
        try {
            // Validating a hash
            return argon2.verify(hash, messageCharArray);
        } finally {
            // Wipe confidential data
            argon2.wipeArray(messageCharArray);
        }
    }
}
