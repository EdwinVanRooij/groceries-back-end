package me.evrooij.managers;

import me.evrooij.domain.User;

import java.util.*;

public class UserManager {

    private Map<String, User> users = new HashMap<>();

    public UserManager() {
        for (int i = 0; i < 3; i++) {
            createUser(String.format("Name %s", i), String.format("Mail %s", i));
        }
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUser(String id) {
        return users.get(id);
    }

    public User createUser(String name, String email) {
        failIfInvalid(name, email);
        User user = new User(name, email);
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(String id, String name, String email) {
        User user = users.get(id);
        if (user == null) {
            throw new IllegalArgumentException("No user with id '" + id + "' found");
        }
        failIfInvalid(name, email);
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    private void failIfInvalid(String name, String email) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'name' cannot be empty");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'email' cannot be empty");
        }
    }
}
