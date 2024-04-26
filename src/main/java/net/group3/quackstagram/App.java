package net.group3.quackstagram;

import javax.swing.SwingUtilities;

import net.group3.quackstagram.backend.database.DataFacade;
import net.group3.quackstagram.backend.database.DataSourceFactory;
import net.group3.quackstagram.ui.SignInUI;

public class App {
    public static void main(String[] args) {

        // Initialize DataSourceFactory and create DataSources
        DataSourceFactory dataSourceFactory = new DataSourceFactory();

        // Initialize the DataFacade
        DataFacade dataFacade = new DataFacade(dataSourceFactory);

        SwingUtilities.invokeLater(() -> {
            SignInUI frame = new SignInUI(dataFacade);
            frame.setVisible(true);
        });
    }
}
