package net.group3.quackstagram.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.group3.quackstagram.backend.database.DataFacade;

public class SignUpUI extends UI {

    private JTextField txtUsername;
    private JTextField txtPassword;
    private JTextField txtBio;
    private JButton btnRegister;
    private JLabel lblPhoto;
    private JButton btnUploadPhoto;
    private final String credentialsFilePath = "data/credentials.txt";
    private final String profilePhotoStoragePath = "img/storage/profile/";
    private JButton btnSignIn;

    public SignUpUI(DataFacade data) {
        super("Quackstagram - Register", data);
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
        txtBio = new JTextField("Bio");
        txtBio.setForeground(Color.GRAY);
        txtUsername.setForeground(Color.GRAY);
        txtPassword.setForeground(Color.GRAY);

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(photoPanel);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtUsername);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtBio);
        btnUploadPhoto = new JButton("Upload Photo");

        btnUploadPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleProfilePictureUpload();
            }
        });
        JPanel photoUploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        photoUploadPanel.add(btnUploadPhoto);
        fieldsPanel.add(photoUploadPanel);

        // Adding components to the frame
        return fieldsPanel;
    }

    @Override
    protected JPanel createNavigationPanel() {
        // Register button with black text
        btnRegister = new JButton("Register");
        btnRegister.addActionListener(this::onRegisterClicked);
        btnRegister.setBackground(new Color(255, 90, 95)); // Use a red color that matches the mockup
        btnRegister.setForeground(Color.BLACK); // Set the text color to black
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel registerPanel = new JPanel(new BorderLayout()); // Panel to contain the register button
        registerPanel.setBackground(Color.WHITE); // Background for the panel
        registerPanel.add(btnRegister, BorderLayout.CENTER);

        // Adding the sign in button to the register panel or another suitable panel
        btnSignIn = new JButton("Already have an account? Sign In");
        btnSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignInUI();
            }
        });
        registerPanel.add(btnSignIn, BorderLayout.SOUTH);
        return registerPanel;
    }

    private void onRegisterClicked(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String bio = txtBio.getText();

        if (doesUsernameExist(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            saveCredentials(username, password, bio);
            handleProfilePictureUpload();
            dispose();

            // Open the SignInUI frame
            SwingUtilities.invokeLater(() -> {
                SignInUI signInFrame = new SignInUI(this.data);
                signInFrame.setVisible(true);
            });
        }
    }

    private boolean doesUsernameExist(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username + ":")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to handle profile picture upload
    private void handleProfilePictureUpload() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            saveProfilePicture(selectedFile, txtUsername.getText());
        }
    }

    private void saveProfilePicture(File file, String username) {
        try {
            BufferedImage image = ImageIO.read(file);
            File outputFile = new File(profilePhotoStoragePath + username + ".png");
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCredentials(String username, String password, String bio) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/credentials.txt", true))) {
            writer.write(username + ":" + password + ":" + bio);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSignInUI() {
        // Close the SignUpUI frame
        dispose();

        // Open the SignInUI frame
        SwingUtilities.invokeLater(() -> {
            SignInUI signInFrame = new SignInUI(this.data);
            signInFrame.setVisible(true);
        });
    }

}
