package net.group3.quackstagram.ui;

import javax.swing.*;

import net.group3.quackstagram.backend.database.DataFacade;
import net.group3.quackstagram.models.User;
import net.group3.quackstagram.models.Post;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class InstagramProfileUI extends UI {

    private static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
    private static final int GRID_IMAGE_SIZE = WIDTH / 3; // Static size for grid images

    public InstagramProfileUI(User user, DataFacade data) {
        super("DACS Profile", user, data);
        try {
            // Fetch user details and stats from the database
            loggedInUser = data.findUser(loggedInUser.getUserId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initializeUI();
    }

    public InstagramProfileUI(DataFacade data) {
        super("DACS Profile", data);
        try {
            // Fetch user details and stats from the database
            loggedInUser = data.findUser(loggedInUser.getUserId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initializeUI();
    }

    // Override Draw content methods
    @Override
    protected JPanel createHeaderPanel() {
        boolean isLoggedInUser = false;

        // Header Panel
        JPanel headerPanel = new JPanel();
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

        try {
            int postsCount = data.findPostsByUserId(loggedInUser.getUserId()).size();
            int followersCount = data.getFollowersCount(loggedInUser.getUserId());
            int followingCount = data.getFollowingCount(loggedInUser.getUserId());

            statsPanel.add(createStatLabel(Integer.toString(postsCount), "Posts"));
            statsPanel.add(createStatLabel(Integer.toString(followersCount), "Followers"));
            statsPanel.add(createStatLabel(Integer.toString(followingCount), "Following"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding

        // Follow Button
        // Follow or Edit Profile Button
        JButton followButton;
        try {
            isLoggedInUser = data.isFollowing(loggedInUser.getUserId(), loggedInUser.getUserId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (isLoggedInUser) {
            followButton = new JButton("Edit Profile");
        } else {
            followButton = new JButton("Follow");
            followButton.addActionListener(e -> {
                try {
                    data.followUser(loggedInUser.getUserId(), loggedInUser.getUserId());
                    followButton.setText("Following");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
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

        try {
            List<Post> posts = data.findPostsByUserId(loggedInUser.getUserId());
            for (Post post : posts) {
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(post.getImagePath()).getImage()
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        try {
            User userToFollow = data.findUserByUsername(usernameToFollow);
            if (userToFollow != null) {
                data.followUser(loggedInUser.getUserId(), userToFollow.getUserId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
