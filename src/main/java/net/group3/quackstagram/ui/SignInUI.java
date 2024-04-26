package net.group3.quackstagram.ui;

import javax.swing.*;

import net.group3.quackstagram.backend.database.DataFacade;
import net.group3.quackstagram.models.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SignInUI extends UI {

    private JTextField txtUsername;
    private JTextField txtPassword;
    private JButton btnSignIn, btnRegisterNow;
    private JLabel lblPhoto;

    private User newUser;

    public SignInUI(DataFacade data) {
        super("Quackstagram - Sign In", data);
    }

    @Override
    protected JPanel createMainContentPanel() {
        // Profile picture placeholder without border
        lblPhoto = new JLabel();
        lblPhoto.setPreferredSize(new Dimension(80, 80));
        lblPhoto.setHorizontalAlignment(JLabel.CENTER);
        lblPhoto.setVerticalAlignment(JLabel.CENTER);
        lblPhoto.setIcon(new ImageIcon(
                new ImageIcon("img/logos/DACS.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        JPanel photoPanel = new JPanel(); // Use a panel to center the photo label
        photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        photoPanel.add(lblPhoto);

        // Text fields panel
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        txtUsername = new JTextField("Username");
        txtPassword = new JTextField("Password");
        txtUsername.setForeground(Color.GRAY);
        txtPassword.setForeground(Color.GRAY);

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(photoPanel);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtUsername);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(Box.createVerticalStrut(10));

        return fieldsPanel;
    }

    @Override
    protected void addPanelsToFrame() {
        this.headerPanel = createHeaderPanel();
        this.contentPanel = createMainContentPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // Bottom
        // Register button with black text
        btnSignIn = new JButton("Sign-In");
        btnSignIn.addActionListener(this::onSignInClicked);
        btnSignIn.setBackground(new Color(255, 90, 95)); // Use a red color that matches the mockup
        btnSignIn.setForeground(Color.BLACK); // Set the text color to black
        btnSignIn.setFocusPainted(false);
        btnSignIn.setBorderPainted(false);
        btnSignIn.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel registerPanel = new JPanel(new BorderLayout()); // Panel to contain the register button
        registerPanel.setBackground(Color.WHITE); // Background for the panel
        registerPanel.add(btnSignIn, BorderLayout.CENTER);

        // Panel to hold both buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // Grid layout with 1 row, 2 columns

        // New button for navigating to SignUpUI
        btnRegisterNow = new JButton("No Account? Register Now");
        btnRegisterNow.addActionListener(this::onRegisterNowClicked);
        btnRegisterNow.setBackground(Color.WHITE); // Set a different color for distinction
        btnRegisterNow.setForeground(Color.BLACK);
        btnRegisterNow.setFocusPainted(false);
        btnRegisterNow.setBorderPainted(false);
        buttonPanel.setBackground(Color.white);
        buttonPanel.add(btnSignIn);
        buttonPanel.add(btnRegisterNow);

        add(registerPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshUI();
    }

    private void onSignInClicked(ActionEvent event) {
        String enteredUsername = txtUsername.getText();
        String enteredPassword = txtPassword.getText();
        System.out.println(enteredUsername + " <-> " + enteredPassword);
        if (verifyCredentials(enteredUsername, enteredPassword)) {
            System.out.println("It worked");
            // Close the SignUpUI frame
            dispose();

            // Open the SignInUI frame
            SwingUtilities.invokeLater(() -> {
                InstagramProfileUI profileUI = new InstagramProfileUI(newUser, this.data);
                profileUI.setVisible(true);
            });
        } else {
            System.out.println("It Didn't");
        }
    }

    private void onRegisterNowClicked(ActionEvent event) {
        // Close the SignInUI frame
        dispose();

        // Open the SignUpUI frame
        SwingUtilities.invokeLater(() -> {
            SignUpUI signUpFrame = new SignUpUI(this.data);
            signUpFrame.setVisible(true);
        });
    }

    private boolean verifyCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/credentials.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(":");
                if (credentials[0].equals(username) && credentials[1].equals(password)) {
                    String bio = credentials[2];
                    // Create User object and save information
                    newUser = new User(username, bio, password); // Assuming User constructor takes these parameters
                    saveUserInformation(newUser);

                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveUserInformation(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/users.txt", false))) {
            writer.write(user.toString()); // Implement a suitable toString method in User class
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
