package me.evrooij.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

/**
 * Created by eddy on 27-11-16.
 */

@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private int id;
    @Expose
    private String message;

    // GSON annotation for deserialization
    @SerializedName("type")
    @Expose
    private Type type;

    @ManyToOne
    @Expose
    private Account sender;

    public Feedback(String message, Type type, Account sender) {
        this.message = message;
        this.type = type;
        this.sender = sender;
    }

    public Feedback() {
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }

    public Account getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return String.format("Feedback type %s by %s, message %s", type.toString(), sender.toString(), message);
    }

    public enum Type {
        @SerializedName("0")
        Suggestion,
        @SerializedName("1")
        Bug
    }
}
