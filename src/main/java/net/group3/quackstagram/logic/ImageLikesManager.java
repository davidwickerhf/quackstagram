package net.group3.quackstagram.logic;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

// TODO This and UserRelationshipManager could have a parent class to avoid code duplication
public class ImageLikesManager {

    private final String likesFilePath = "data/likes.txt";

    // Method to like an image
    public void likeImage(String username, String imageID) throws IOException {
        Map<String, Set<String>> likesMap = readLikes();
        if (!likesMap.containsKey(imageID)) {
            likesMap.put(imageID, new HashSet<>());
        }
        Set<String> users = likesMap.get(imageID);
        if (users.add(username)) { // Only add and save if the user hasn't already liked the image
            saveLikes(likesMap);
        }
    }

    // Method to read likes from file
    private Map<String, Set<String>> readLikes() throws IOException {
        Map<String, Set<String>> likesMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(likesFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String imageID = parts[0];
                Set<String> users = Arrays.stream(parts[1].split(",")).collect(Collectors.toSet());
                likesMap.put(imageID, users);
            }
        }
        return likesMap;
    }

    // Method to save likes to file
    private void saveLikes(Map<String, Set<String>> likesMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(likesFilePath, false))) {
            for (Map.Entry<String, Set<String>> entry : likesMap.entrySet()) {
                String line = entry.getKey() + ":" + String.join(",", entry.getValue());
                writer.write(line);
                writer.newLine();
            }
        }
    }

}
