package me.evrooij.exceptions;

/**
 * @author eddy on 5-1-17.
 */
public class InvalidLoginCredentialsException extends Exception {
    public InvalidLoginCredentialsException(String message) {
        super(message);
    }
}
