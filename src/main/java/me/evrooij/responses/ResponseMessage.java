package me.evrooij.responses;

/**
 * @author eddy on 6-12-16.
 *         Small helper class which converts an exception or unformatted string message to a complete string field
 */
public class ResponseMessage {
    // Method is used by json converter to show it's message
    @SuppressWarnings("unused")
    private String message;

    public ResponseMessage(String message, String... args) {
        this.message = String.format(message, (Object) args);
    }

    public ResponseMessage(Exception e) {
        this.message = e.getMessage();
    }
}
