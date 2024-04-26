package net.group3.quackstagram.backend.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.group3.quackstagram.models.Picture;

/**
 * A repository implementation for managing pictures using SQL database.
 */
public class SQLPictureRepository implements PictureRepository {
    private DataSource dataSource;

    /**
     * Constructs a new SQLPictureRepository with the given data source.
     * 
     * @param dataSource the data source to be used for database connections
     */
    public SQLPictureRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Picture createPicture(Picture picture) {
        final String sql = "INSERT INTO Pictures (username, description, path) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, picture.getUsername());
            pstmt.setString(2, picture.getCaption());
            pstmt.setString(3, picture.getImagePath());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        picture.setKey(generatedKeys.getInt(1));
                        return picture;
                    }
                }
            }
        } catch (SQLException e) {
            // Proper error handling goes here
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Picture findPictureById(int id) {
        final String sql = "SELECT * FROM Pictures WHERE key = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Picture(
                            rs.getInt("key"),
                            rs.getString("username"),
                            rs.getString("description"),
                            rs.getString("path"));
                }
            }
        } catch (SQLException e) {
            // Proper error handling goes here
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Picture> findAllPictures() {
        List<Picture> pictures = new ArrayList<>();
        final String sql = "SELECT * FROM Pictures";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                pictures.add(new Picture(
                        rs.getInt("key"),
                        rs.getString("username"),
                        rs.getString("description"),
                        rs.getString("path")));
            }
        } catch (SQLException e) {
            // Proper error handling goes here
            e.printStackTrace();
        }
        return pictures;
    }

    @Override
    public List<Picture> findPicturesByUser(String username) {
        List<Picture> pictures = new ArrayList<>();
        final String sql = "SELECT * FROM Pictures WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pictures.add(new Picture(
                            rs.getInt("key"),
                            rs.getString("username"),
                            rs.getString("description"),
                            rs.getString("path")));
                }
            }
        } catch (SQLException e) {
            // Proper error handling goes here
            e.printStackTrace();
        }
        return pictures;
    }

    @Override
    public Picture updatePicture(Picture picture) {
        final String sql = "UPDATE Pictures SET description = ?, path = ? WHERE key = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, picture.getCaption());
            pstmt.setString(2, picture.getImagePath());
            pstmt.setInt(3, picture.getKey());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return picture;
            }
        } catch (SQLException e) {
            // Proper error handling goes here
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deletePicture(int id) {
        final String sql = "DELETE FROM Pictures WHERE key = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Proper error handling goes here
            e.printStackTrace();
        }
    }

    @Override
    public List<Picture> findRecentPictures(int limit) {
        List<Picture> pictures = new ArrayList<>();
        String sql = "SELECT * FROM Posts ORDER BY timestamp DESC LIMIT ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pictures.add(new Picture(rs.getInt("key"), rs.getString("username"),
                            rs.getString("description"), rs.getString("path")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Proper logging and error handling should be done here.
        }

        return pictures;
    }

    @Override
    public List<Picture> findPicturesWithMostLikes(int limit) {
        List<Picture> pictures = new ArrayList<>();
        String sql = "SELECT Posts.*, COUNT(Likes.postKey) AS like_count " +
                "FROM Posts LEFT JOIN Likes ON Posts.key = Likes.postKey " +
                "GROUP BY Posts.key " +
                "ORDER BY like_count DESC " +
                "LIMIT ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pictures.add(new Picture(rs.getInt("key"), rs.getString("username"),
                            rs.getString("description"), rs.getString("path")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Proper logging and error handling should be done here.
        }

        return pictures;
    }

    @Override
    public void likePicture(int pictureId, String username) {
        String sql = "INSERT INTO Likes (username_from, postKey) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setInt(2, pictureId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Proper logging and error handling should be done here.
        }
    }

    @Override
    public void unlikePicture(int pictureId, String username) {
        String sql = "DELETE FROM Likes WHERE username_from = ? AND postKey = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setInt(2, pictureId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Proper logging and error handling should be done here.
        }
    }

    @Override
    public int countLikes(int pictureId) {
        String sql = "SELECT COUNT(*) FROM Likes WHERE postKey = ?";
        int count = 0;

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, pictureId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Proper logging and error handling should be done here.
        }

        return count;
    }

    // Implement the UserRepository methods with JDBC operations...

}
