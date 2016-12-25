package me.evrooij.domain;

import sun.plugin2.message.GetAppletMessage;

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
     * Algorithm to match a search query with this account
     *
     * @param searcherId  the searcher must not be included
     * @param searchQuery the user entered search query, matches on:
     *                    (all of these are case insensitive)
     *                    - query equals username
     *                    - query equals email
     *                    - query partially equals username
     *                    - query partially equals email
     * @return true on match, false on no match
     */
    public boolean matchesFriendSearch(int searcherId, String searchQuery) {
        // If this account is the account who's searching, don't match
        if (getId() == searcherId) {
            return false;
        }

        // Match on query equals username
        if (searchQuery.toLowerCase().matches(getUsername().toLowerCase())) {
            return true;
        }
        // Match on query equals email
        if (searchQuery.toLowerCase().matches(getEmail().toLowerCase())) {
            return true;
        }

        // Match on partially equals username
        if (getUsername().toLowerCase().contains(searchQuery.toLowerCase())) {
            return true;
        }
        // Match on partially equals email
        if (getEmail().toLowerCase().contains(searchQuery.toLowerCase())) {
            return true;
        }

        // Didn't match any of the above, so it doesn't match this account
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s (%s), %s, %s", getUsername(), getId(), getEmail(), getPassword());
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
