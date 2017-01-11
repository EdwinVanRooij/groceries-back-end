package me.evrooij.data;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eddy on 27-11-16.
 */

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private int id;
    @Expose
    private String username;
    @Expose
    private String email;
    private String password;

    @ManyToMany
    private List<Account> friends;

    public Account(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        friends = new ArrayList<>();
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

    public List<Account> getFriends() {
        return friends;
    }

    /**
     * Checks if account is friends with
     *
     * @param friendId id of the account to check friends with for
     * @return true if friendId account is a friend, false if he/she's not
     */
    public boolean isFriendsWith(int friendId) {
        for (Account friend : friends) {
            if (friend.getId() == friendId) {
                // Friend is already in this friend's list, return positive
                return true;
            }
        }
        // No friend found for this id, return negative
        return false;
    }

    /**
     * Adds a friend to this account
     *
     * @param friend
     */
    @SuppressWarnings("JavaDoc")
    public void addFriend(Account friend) {
        friends.add(friend);
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
     *                    Does not match:
     *                    - own account
     *                    - accounts which are already friends
     * @return true on match, false on no match
     */
    public boolean matchesFriendSearch(int searcherId, String searchQuery) {
        System.out.println("Starting matches friend...");

        // If this account is the account who's searching, don't match
        if (getId() == searcherId) {
            System.out.println("Same id");
            return false;
        }

        // If the account already is a friend, don't match
        if (isFriendsWith(searcherId)) {
            System.out.println("already friend");
            return false;
        }

        // Match on query equals username
        if (searchQuery.toLowerCase().matches(getUsername().toLowerCase())) {
            System.out.println(String.format("Matching true on query %s to username %s", searchQuery, username));
            return true;
        }
        // Match on query equals email
        if (searchQuery.toLowerCase().matches(getEmail().toLowerCase())) {
            System.out.println(String.format("Matching true on query %s to mail %s", searchQuery, email));
            return true;
        }

        // Match on partially equals username
        if (getUsername().toLowerCase().contains(searchQuery.toLowerCase())) {
            System.out.println(String.format("Matching true on query %s to partial username %s", searchQuery, username));
            return true;
        }
        // Match on partially equals email
        if (getEmail().toLowerCase().contains(searchQuery.toLowerCase())) {
            System.out.println(String.format("Matching true on query %s to partial mail %s", searchQuery, email));
            return true;
        }

        System.out.println("Didn't match any friends...");

        // Didn't match any of the above, so it doesn't match this account
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s (%s), %s", getUsername(), getId(), getEmail());
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
