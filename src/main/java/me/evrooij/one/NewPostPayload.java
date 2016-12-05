package me.evrooij.one;

import java.util.LinkedList;
import java.util.List;

/**
 * @author eddy on 30-11-16.
 */
public class NewPostPayload implements Validable {
    private String title;
    private List<String> categories = new LinkedList<>();
    private String content;

    public String getTitle() {
        return title;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getContent() {
        return content;
    }

    public boolean isValid() {
        return title != null && !title.isEmpty() && !categories.isEmpty();
    }
}
