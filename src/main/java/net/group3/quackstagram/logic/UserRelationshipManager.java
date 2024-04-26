package net.group3.quackstagram.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRelationshipManager {

    private final String followersFilePath = "data/followers.txt";

    // Method to follow a user
    public void followUser(String follower, String followed) throws IOException {
        if (!isAlreadyFollowing(follower, followed)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(followersFilePath, true))) {
                writer.write(follower + ":" + followed);
                writer.newLine();
            }
        }
    }

    // Method to check if a user is already following another user
    private boolean isAlreadyFollowing(String follower, String followed) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(follower + ":" + followed)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Method to get the list of followers for a user
    public List<String> getFollowers(String username) throws IOException {
        List<String> followers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[1].equals(username)) {
                    followers.add(parts[0]);
                }
            }
        }
        return followers;
    }

    // Method to get the list of users a user is following
    public List<String> getFollowing(String username) throws IOException {
        List<String> following = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(username)) {
                    following.add(parts[1]);
                }
            }
        }
        return following;
    }
}
