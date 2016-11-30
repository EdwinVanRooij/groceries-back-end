package me.evrooij;

import java.util.List;

/**
 * @author eddy on 30-11-16.
 */
public class Post {
    private int id;
    private String title;
    private List<String> categories;
    private String content;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getContent() {
        return content;
    }

    public Post(int id, String title, List<String> categories, String content) {
        this.id = id;
        this.title = title;
        this.categories = categories;
        this.content = content;
    }


}
