package me.evrooij.data;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by eddy on 22-11-16.
 */

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    private int amount;

    @Expose
    @ManyToOne
    private Account owner;

    @Expose
    private String comment;

    @Expose
    private Date deletionDate;

    @Expose
    private String imageUrl;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getComment() {
        return comment;
    }

    public Account getOwner() {
        return owner;
    }

    public Date getDeletionDate() {
        return deletionDate;
    }


    public Product() {
    }

    public Product(String name, int amount, String comment, Account owner) {
        this.name = name;
        this.amount = amount;
        this.owner = owner;
        this.comment = comment;
    }
    public Product(String name, int amount, String comment, Account owner, String imageUrl) {
        this.name = name;
        this.amount = amount;
        this.owner = owner;
        this.comment = comment;
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Useful for testing purposes, we'll consider items equal on the same fields
     *
     * @param obj other product
     * @return true if objects are equal, false if not
     */
    @Override
    public boolean equals(Object obj) {
        Product other = (Product) obj;
        // If all fields are the same, return true
//        return other.getId() == getId() // If all fields are the same, return true
        return other.getName().equals(getName())
                && other.getAmount() == getAmount()
                && other.getOwner().equals(getOwner())
                && other.getComment().equals(getComment())
                || super.equals(obj);
    }

    @Override
    public String toString() {
        if (imageUrl == null) {
            return String.format("%s, id %s - %s times of %s, %s", getName(), getId(), getAmount(), getOwner(), getComment());
        } else {
            return String.format("%s, id %s - %s times of %s, %s, url %s", getName(), getId(), getAmount(), getOwner(), getComment(), imageUrl);
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
