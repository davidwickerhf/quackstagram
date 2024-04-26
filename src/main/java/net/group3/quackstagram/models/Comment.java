package net.group3.quackstagram.models;

import java.util.Date;

public class Comment {
    private int key;
    private int postKey;
    private String usernameFrom;
    private String usernameTo;
    private String text;
    private Date timestamp;

    public Comment(int key, String usernameFrom, String text, int postKey) {
        this.key = key;
        this.usernameFrom = usernameFrom;
        this.text = text;
        this.postKey = postKey;
        this.timestamp = new Date(); // Set current date and time
    }

    // Assume getters and setters are here...
}