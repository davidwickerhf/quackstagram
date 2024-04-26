package net.group3.quackstagram.models;

import java.util.List;
import java.util.ArrayList;

// Represents a picture on Quackstagram
public class Picture {
    private int key;
    private String username;
    private String imagePath;
    private String caption;
    private int likesCount;
    private List<String> comments;
    private List<Comment> commentList;

    public Picture(String imagePath, String caption) {
        this.imagePath = imagePath;
        this.caption = caption;
        this.likesCount = 0;
        this.comments = new ArrayList<>();
    }

    public Picture(int key, String username, String imagePath, String caption) {
        this.key = key;
        this.username = username;
        this.imagePath = imagePath;
        this.caption = caption;
    }

    // Getter methods for picture details
    public int getKey() {
        return key;
    }

    public String getUsername() {
        return username;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getCaption() {
        return caption;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public List<String> getComments() {
        return comments;
    }

    // Setter methods for picture details
    public void setKey(int key) {
        this.key = key;
    }
}
