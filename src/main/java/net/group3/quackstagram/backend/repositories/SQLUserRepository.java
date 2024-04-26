package net.group3.quackstagram.backend.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.group3.quackstagram.models.User;

public class SQLUserRepository implements UserRepository {
    private DataSource dataSource;

    public SQLUserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO Users (username, password_hashed, bio, picturePath) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getBio());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user; // Assuming user has been created successfully.
    }

    @Override
    public User findUserByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("username"), rs.getString("password_hashed"),
                            rs.getString("bio"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // User not found or an error occurred
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(rs.getString("username"), rs.getString("password_hashed"),
                        rs.getString("bio")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE Users SET password_hashed = ?, bio = ?, picturePath = ? WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getBio());
            pstmt.setString(4, user.getUsername());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user; // Assuming user has been updated successfully.
    }

    @Override
    public void deleteUser(String username) {
        String sql = "DELETE FROM Users WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> findUsersFollowing(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findUsersFollowing'");
    }

    @Override
    public List<User> findUsersFollowedBy(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findUsersFollowedBy'");
    }

    @Override
    public boolean authenticateUser(String username, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'authenticateUser'");
    }

    @Override
    public void updatePassword(String username, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePassword'");
    }

    @Override
    public void updateBio(String username, String newBio) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBio'");
    }

    @Override
    public void updateProfilePicture(String username, String newPicturePath) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProfilePicture'");
    }

    // Implement the UserRepository methods with JDBC operations...
}
