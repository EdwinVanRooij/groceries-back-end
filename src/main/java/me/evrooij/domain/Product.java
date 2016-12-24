package me.evrooij.domain;

import javax.persistence.*;

/**
 * Created by eddy on 22-11-16.
 */

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int amount;
    private String owner;
    private String comment;

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

    public String getOwner() {
        return owner;
    }

    public Product() {
    }

    public Product(String name, int amount, String comment, String owner) {
        this.name = name;
        this.amount = amount;
        this.owner = owner;
        this.comment = comment;
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
        return String.format("%s, id %s - %s times of %s, %s", getName(), getId(), getAmount(), getOwner(), getComment());
    }
}
