package me.evrooij.data;

import com.google.gson.annotations.Expose;

/**
 * @author eddy on 6-12-16.
 *         Small helper class which converts an exception or unformatted string message to a complete string field
 */
public class ResponseMessage {
    // Method is used by json2 converter to show it's message
    @SuppressWarnings("unused")
    @Expose
    private String message;

    public ResponseMessage(String message, String... args) {
        this.message = String.format(message, (Object) args);
    }

    public ResponseMessage(Exception e) {
        this.message = e.getMessage();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object obj) {
        ResponseMessage other = (ResponseMessage) obj;
        if (other.getMessage().equals(getMessage())) {
            return true;
        }
        return super.equals(obj);
    }
}
