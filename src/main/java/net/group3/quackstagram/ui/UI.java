package net.group3.quackstagram.ui;

import javax.swing.*;

import net.group3.quackstagram.backend.database.DataFacade;
import net.group3.quackstagram.models.User;

import java.awt.*;
import java.sql.SQLException;

public abstract class UI extends JFrame {
    protected static final int WIDTH = 300;
    protected static final int HEIGHT = 500;
    protected static final int NAV_ICON_SIZE = 20;
    protected static final int IMAGE_SIZE = WIDTH / 3;

    protected JPanel contentPanel;
    protected JPanel headerPanel;
    protected JPanel navigationPanel;

    protected String title;
    protected User loggedInUser;

    protected DataFacade data;

    public UI(String title, DataFacade data) {
        this.data = data;
        this.title = title;
        configureFrame();
        initializeUI();
    }

    public UI(String title, User user, DataFacade data) {
        this.data = data;
        this.title = title;
        this.loggedInUser = user;
        configureFrame();
        initializeUI();
    }

    // Draw content methods
    protected abstract JPanel createMainContentPanel();

    protected JPanel createHeaderPanel() {
        // Header Panel (reuse from InstagramProfileUI or customize for home page)
        // Header with the Register label
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        JLabel lblRegister = new JLabel(String.format(" %s 🐥", this.title));
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white
        headerPanel.add(lblRegister);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
        return headerPanel;
    }

    protected JPanel createNavigationPanel() {
        // Create and return the navigation panel
        // Navigation Bar
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(new Color(249, 249, 249));
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        navigationPanel.add(createIconButton("img/icons/home.png", "home"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton("img/icons/search.png", "explore"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton("img/icons/add.png", "add"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton("img/icons/heart.png", "notification"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton("img/icons/profile.png", "profile"));

        return navigationPanel;
    }

    // UI Utility methods
    protected void configureFrame() {
        setTitle(this.title);
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    protected void initializeUI() {
        resetContentPane();
        addPanelsToFrame();
    }

    protected void resetContentPane() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());
    }

    protected void addPanelsToFrame() {
        this.headerPanel = createHeaderPanel();
        this.contentPanel = createMainContentPanel();
        this.navigationPanel = createNavigationPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
        refreshUI();
    }

    protected void refreshUI() {
        revalidate();
        repaint();
    }

    // Utility methods
    protected JButton createIconButton(String iconPath, String buttonType) {
        ImageIcon iconOriginal = new ImageIcon(iconPath);
        Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(iconScaled));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);

        // Define actions based on button type
        if ("home".equals(buttonType)) {
            button.addActionListener(e -> openHomeUI());
        } else if ("profile".equals(buttonType)) {
            button.addActionListener(e -> openProfileUI());
        } else if ("notification".equals(buttonType)) {
            button.addActionListener(e -> notificationsUI());
        } else if ("explore".equals(buttonType)) {
            button.addActionListener(e -> exploreUI());
        } else if ("add".equals(buttonType)) {
            button.addActionListener(e -> ImageUploadUI());
        }
        return button;

    }

    protected void displayImage(ImageIcon imageIcon) {
        contentPanel.removeAll(); // Remove existing content
        contentPanel.setLayout(new BorderLayout()); // Change layout for image display

        JLabel fullSizeImageLabel = new JLabel(imageIcon);
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(fullSizeImageLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            getContentPane().removeAll(); // Remove all components from the frame
            initializeUI(); // Re-initialize the UI
        });
        contentPanel.add(backButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    // Navigation methods
    protected void ImageUploadUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        ImageUploadUI upload = new ImageUploadUI(this.loggedInUser, this.data);
        upload.setVisible(true);
    }

    protected void openProfileUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        InstagramProfileUI profileUI = new InstagramProfileUI(this.loggedInUser, this.data);
        profileUI.setVisible(true);
    }

    protected void notificationsUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        NotificationsUI notificationsUI = new NotificationsUI(this.loggedInUser, this.data);
        notificationsUI.setVisible(true);
    }

    protected void openHomeUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        QuakstagramHomeUI homeUI = new QuakstagramHomeUI(this.loggedInUser, this.data);
        homeUI.setVisible(true);
    }

    protected void exploreUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        ExploreUI explore = new ExploreUI(this.loggedInUser, this.data);
        explore.setVisible(true);
    }
}
