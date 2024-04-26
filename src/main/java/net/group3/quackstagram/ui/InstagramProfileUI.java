package net.group3.quackstagram.ui;

import javax.swing.*;

import net.group3.quackstagram.backend.database.DataFacade;
import net.group3.quackstagram.models.User;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.awt.*;
import java.nio.file.*;
import java.util.stream.Stream;

public class InstagramProfileUI extends UI {

    private static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
    private static final int GRID_IMAGE_SIZE = WIDTH / 3; // Static size for grid images

    public InstagramProfileUI(User user, DataFacade data) {
        super("DACS Profile", user, data);

        // Initialize counts
        int imageCount = 0;
        int followersCount = 0;
        int followingCount = 0;

        // Step 1: Read image_details.txt to count the number of images posted by the
        // user
        Path imageDetailsFilePath = Paths.get("img", "image_details.txt");
        try (BufferedReader imageDetailsReader = Files.newBufferedReader(imageDetailsFilePath)) {
            String line;
            while ((line = imageDetailsReader.readLine()) != null) {
                if (line.contains("Username: " + loggedInUser.getUsername())) {
                    imageCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Step 2: Read following.txt to calculate followers and following
        Path followingFilePath = Paths.get("data", "following.txt");
        try (BufferedReader followingReader = Files.newBufferedReader(followingFilePath)) {
            String line;
            while ((line = followingReader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String[] followingUsers = parts[1].split(";");
                    if (username.equals(loggedInUser.getUsername())) {
                        followingCount = followingUsers.length;
                    } else {
                        for (String followingUser : followingUsers) {
                            if (followingUser.trim().equals(loggedInUser.getUsername())) {
                                followersCount++;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String bio = "";

        Path bioDetailsFilePath = Paths.get("data", "credentials.txt");
        try (BufferedReader bioDetailsReader = Files.newBufferedReader(bioDetailsFilePath)) {
            String line;
            while ((line = bioDetailsReader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(loggedInUser.getUsername()) && parts.length >= 3) {
                    bio = parts[2];
                    break; // Exit the loop once the matching bio is found
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Bio for " + loggedInUser.getUsername() + ": " + bio);
        loggedInUser.setBio(bio);

        loggedInUser.setFollowersCount(followersCount);
        loggedInUser.setFollowingCount(followingCount);
        loggedInUser.setPostsCount(imageCount);

        System.out.println(loggedInUser.getPostsCount());
    }

    public InstagramProfileUI(DataFacade data) {
        super("DACS Profile", data);
    }

    // Override Draw content methods
    @Override
    protected JPanel createHeaderPanel() {
        boolean isloggedInUser = false;
        String loggedInUsername = "";

        // Read the logged-in user's username from users.txt
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                loggedInUsername = line.split(":")[0].trim();
                isloggedInUser = loggedInUsername.equals(loggedInUser.getUsername());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Header Panel
        JPanel headerPanel = new JPanel();
        try (Stream<String> lines = Files.lines(Paths.get("data", "users.txt"))) {
            isloggedInUser = lines.anyMatch(line -> line.startsWith(loggedInUser.getUsername() + ":"));
        } catch (IOException e) {
            e.printStackTrace(); // Log or handle the exception as appropriate
        }

        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.GRAY);

        // Top Part of the Header (Profile Image, Stats, Follow Button)
        JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(new Color(249, 249, 249));

        // Profile image
        ImageIcon profileIcon = new ImageIcon(
                new ImageIcon("img/storage/profile/" + loggedInUser.getUsername() + ".png")
                        .getImage().getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topHeaderPanel.add(profileImage, BorderLayout.WEST);

        // Stats Panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statsPanel.setBackground(new Color(249, 249, 249));
        System.out.println("Number of posts for this user" + loggedInUser.getPostsCount());
        statsPanel.add(createStatLabel(Integer.toString(loggedInUser.getPostsCount()), "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(loggedInUser.getFollowersCount()), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(loggedInUser.getFollowingCount()), "Following"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding

        // Follow Button
        // Follow or Edit Profile Button
        // followButton.addActionListener(e ->
        // handleFollowAction(loggedInUser.getUsername()));
        JButton followButton;
        if (isloggedInUser) {
            followButton = new JButton("Edit Profile");
        } else {
            followButton = new JButton("Follow");

            // Check if the current user is already being followed by the logged-in user
            Path followingFilePath = Paths.get("data", "following.txt");
            try (BufferedReader reader = Files.newBufferedReader(followingFilePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts[0].trim().equals(loggedInUsername)) {
                        String[] followedUsers = parts[1].split(";");
                        for (String followedUser : followedUsers) {
                            if (followedUser.trim().equals(loggedInUser.getUsername())) {
                                followButton.setText("Following");
                                break;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            followButton.addActionListener(e -> {
                handleFollowAction(loggedInUser.getUsername());
                followButton.setText("Following");
            });
        }

        followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        followButton.setFont(new Font("Arial", Font.BOLD, 12));

        // Make the button fill the horizontal space
        followButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, followButton.getMinimumSize().height));
        followButton.setBackground(new Color(225, 228, 232)); // A soft, appealing color that complements the UI
        followButton.setForeground(Color.BLACK);
        followButton.setOpaque(true);
        followButton.setBorderPainted(false);
        followButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some vertical padding

        // Add Stats and Follow Button to a combined Panel
        JPanel statsFollowPanel = new JPanel();
        statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
        statsFollowPanel.add(statsPanel);
        statsFollowPanel.add(followButton);
        topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);

        headerPanel.add(topHeaderPanel);

        // Profile Name and Bio Panel
        JPanel profileNameAndBioPanel = new JPanel();
        profileNameAndBioPanel.setLayout(new BorderLayout());
        profileNameAndBioPanel.setBackground(new Color(249, 249, 249));

        JLabel profileNameLabel = new JLabel(loggedInUser.getUsername());
        profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Padding on the sides

        JTextArea profileBio = new JTextArea(loggedInUser.getBio());
        System.out.println("This is the bio " + loggedInUser.getUsername());
        profileBio.setEditable(false);
        profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
        profileBio.setBackground(new Color(249, 249, 249));
        profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Padding on the sides

        profileNameAndBioPanel.add(profileNameLabel, BorderLayout.NORTH);
        profileNameAndBioPanel.add(profileBio, BorderLayout.CENTER);

        headerPanel.add(profileNameAndBioPanel);

        return headerPanel;
    }

    @Override
    protected JPanel createMainContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 3, 5, 5)); // Grid layout for image grid

        Path imageDir = Paths.get("img", "uploaded");
        try (Stream<Path> paths = Files.list(imageDir)) {
            paths.filter(path -> path.getFileName().toString().startsWith(loggedInUser.getUsername() + "_"))
                    .forEach(path -> {
                        ImageIcon imageIcon = new ImageIcon(new ImageIcon(path.toString()).getImage()
                                .getScaledInstance(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE, Image.SCALE_SMOOTH));
                        JLabel imageLabel = new JLabel(imageIcon);
                        imageLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                System.out.println("Clicked image");
                                displayImage(imageIcon); // Call method to display the clicked image
                            }
                        });
                        contentPanel.add(imageLabel);
                    });
        } catch (IOException ex) {
            ex.printStackTrace();
            // Handle exception (e.g., show a message or log)
        }
        return contentPanel;
    }

    // Override UI Utility methods
    @Override
    protected void addPanelsToFrame() {
        this.headerPanel = createHeaderPanel();
        this.contentPanel = createMainContentPanel();
        this.navigationPanel = createNavigationPanel();

        JScrollPane scrollPane = new JScrollPane(this.contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
        refreshUI();
    }

    // Utility methods
    private JLabel createStatLabel(String number, String text) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + number + "<br/>" + text + "</div></html>",
                SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.BLACK);
        return label;
    }

    private void handleFollowAction(String usernameToFollow) {
        Path followingFilePath = Paths.get("data", "following.txt");
        Path usersFilePath = Paths.get("data", "users.txt");
        String loggedInUserUsername = "";

        try {
            // Read the current user's username from users.txt
            try (BufferedReader reader = Files.newBufferedReader(usersFilePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    loggedInUserUsername = parts[0];
                }
            }

            System.out.println("Real user is " + loggedInUserUsername);
            // If loggedInUserUsername is not empty, process following.txt
            if (!loggedInUserUsername.isEmpty()) {
                boolean found = false;
                StringBuilder newContent = new StringBuilder();

                // Read and process following.txt
                if (Files.exists(followingFilePath)) {
                    try (BufferedReader reader = Files.newBufferedReader(followingFilePath)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(":");
                            if (parts[0].trim().equals(loggedInUserUsername)) {
                                found = true;
                                if (!line.contains(usernameToFollow)) {
                                    line = line.concat(line.endsWith(":") ? "" : "; ").concat(usernameToFollow);
                                }
                            }
                            newContent.append(line).append("\n");
                        }
                    }
                }

                // If the current user was not found in following.txt, add them
                if (!found) {
                    newContent.append(loggedInUserUsername).append(": ").append(usernameToFollow).append("\n");
                }

                // Write the updated content back to following.txt
                try (BufferedWriter writer = Files.newBufferedWriter(followingFilePath)) {
                    writer.write(newContent.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
