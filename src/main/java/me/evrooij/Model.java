package me.evrooij;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author eddy on 30-11-16.
 */
// In a real application you may want to use a DB, for this example we just store the posts in memory
public class Model {
    private int nextId = 1;
    private Map<Integer, Post> posts = new HashMap<>();

    public Model() {
        for (int i = 0; i < 10; i++) {
            posts.put(i + 100, new Post(i + 100, String.format("Title %s", i), new ArrayList<String>(), String.format("This is some content, %s", i)));
        }
    }

    public int createPost(String title, String content, List<String> categories) {
        int id = nextId++;
        Post post = new Post(id, title, categories, content);
        posts.put(id, post);
        return id;
    }

    public List<Post> getAllPosts() {
        return posts.keySet().stream().sorted().map((id) -> posts.get(id)).collect(Collectors.toList());
    }
}
