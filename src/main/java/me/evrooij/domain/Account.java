package me.evrooij.domain;

import javax.persistence.*;

/**
 * Created by eddy on 27-11-16.
 */

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String email;
    private String password;

    public Account(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Account() {
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Consider equal when ID's are equal
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("JavaDoc")
    @Override
    public boolean equals(Object obj) {
        Account other = (Account) obj;
        return getId() == other.getId() || super.equals(obj);
    }
}
