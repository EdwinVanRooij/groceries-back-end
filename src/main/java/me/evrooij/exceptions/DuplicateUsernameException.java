package me.evrooij.exceptions;

/**
 * @author eddy on 5-1-17.
 */
public class DuplicateUsernameException extends Exception {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
