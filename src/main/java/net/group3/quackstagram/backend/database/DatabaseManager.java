package net.group3.quackstagram.backend.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * The DatabaseManager class is responsible for managing the database operations
 * of the Quackstagram application.
 */
public class DatabaseManager {

    private DataSourceFactory dataSourceFactory;

    /**
     * Constructs a new DatabaseManager object and initializes the database.
     */
    public DatabaseManager() {
        dataSourceFactory = new DataSourceFactory();
        initializeDatabase();
    }

    /**
     * Initializes the database by creating tables if they don't exist.
     */
    private void initializeDatabase() {
        // Assuming you're using a SQLite database
        String jdbcUrl = "jdbc:sqlite:database/quackstagram.sqlite";
        dataSourceFactory.createDataSource("database", jdbcUrl);

        // Create tables, apply migrations, or load initial data if necessary
        DataSource dataSource = dataSourceFactory.getDataSource("database");
        createTablesIfNotExist(dataSource);
    }

    /**
     * Creates tables if they don't exist in the database.
     *
     * @param dataSource the DataSource object representing the database connection
     */
    private void createTablesIfNotExist(DataSource dataSource) {
        // ... SQL to create tables if they don't exist
        try (Connection conn = dataSource.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                // Create Users table
                stmt.execute("CREATE TABLE IF NOT EXISTS Users (" +
                        "username TEXT PRIMARY KEY," +
                        "password_hashed TEXT NOT NULL," +
                        "bio TEXT," +
                        "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                        "picturePath TEXT" +
                        ");");

                // Create Posts table
                stmt.execute("CREATE TABLE IF NOT EXISTS Posts (" +
                        "key INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT NOT NULL," +
                        "description TEXT," +
                        "path TEXT," +
                        "FOREIGN KEY (username) REFERENCES Users(username)" +
                        ");");

                // Create Following table
                stmt.execute("CREATE TABLE IF NOT EXISTS Following (" +
                        "username_from TEXT NOT NULL," +
                        "username_to TEXT NOT NULL," +
                        "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                        "PRIMARY KEY (username_from, username_to)," +
                        "FOREIGN KEY (username_from) REFERENCES Users(username)," +
                        "FOREIGN KEY (username_to) REFERENCES Users(username)" +
                        ");");

                // Create Likes table
                stmt.execute("CREATE TABLE IF NOT EXISTS Likes (" +
                        "username_from TEXT NOT NULL," +
                        "postKey INTEGER NOT NULL," +
                        "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                        "PRIMARY KEY (username_from, postKey)," +
                        "FOREIGN KEY (username_from) REFERENCES Users(username)," +
                        "FOREIGN KEY (postKey) REFERENCES Posts(key)" +
                        ");");

                // Create Comments table
                stmt.execute("CREATE TABLE IF NOT EXISTS Comments (" +
                        "key INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username_from TEXT NOT NULL," +
                        "text TEXT NOT NULL," +
                        "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                        "postKey INTEGER NOT NULL," +
                        "FOREIGN KEY (username_from) REFERENCES Users(username)," +
                        "FOREIGN KEY (postKey) REFERENCES Posts(key)" +
                        ");");

                System.out.println("Database tables created successfully.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * Shuts down the DatabaseManager by closing the data sources.
     */
    public void shutdown() {
        dataSourceFactory.shutdown();
    }

    /**
     * Loads the initial data into the database.
     */
    public void loadInitialData() {
        // Load credentials

        // Load following

        // Load notifications

        // Load users

        // Load image details

    }

    // Any additional database management operations...
}
