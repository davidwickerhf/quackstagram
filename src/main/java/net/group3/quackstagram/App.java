package net.group3.quackstagram;

import javax.swing.SwingUtilities;

import net.group3.quackstagram.backend.database.DataFacade;
import net.group3.quackstagram.backend.database.DatabaseConnection;
import net.group3.quackstagram.ui.SignInUI;

public class App {
    public static void main(String[] args) {
        com.zaxxer.hikari.HikariDataSource dataSource = DatabaseConnection.getDataSource();
        // Initialize the DataFacade
        DataFacade dataFacade = new DataFacade(dataSource);

        SwingUtilities.invokeLater(() -> {
            SignInUI frame = new SignInUI(dataFacade);
            frame.setVisible(true);
        });
    }
}
