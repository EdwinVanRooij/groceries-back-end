package me.evrooij.errors;

/**
 * @author eddy on 7-12-16.
 */
public class InvalidAccountException extends Exception {
    public InvalidAccountException(String message) {
        super(message);
    }
}
