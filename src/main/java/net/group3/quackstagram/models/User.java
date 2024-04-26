package net.group3.quackstagram.models;

import java.util.List;
import java.util.ArrayList;

// Represents a user on Quackstagram
public class User {
    private String username;
    private String bio;
    private String password;
    private int postsCount;
    private int followersCount;
    private int followingCount;
    private List<Picture> pictures;

    public User(String username, String bio, String password) {
        this.username = username;
        this.bio = bio;
        this.password = password;
        this.pictures = new ArrayList<>();
        // Initialize counts to 0
        this.postsCount = 0;
        this.followersCount = 0;
        this.followingCount = 0;
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    // Setters
    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPostsCount(int postsCount) {
        this.postsCount = postsCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    // Implement the toString method for saving user information
    @Override
    public String toString() {
        return username + ":" + bio + ":" + password; // Format as needed
    }

    public String getPassword() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
    }

}