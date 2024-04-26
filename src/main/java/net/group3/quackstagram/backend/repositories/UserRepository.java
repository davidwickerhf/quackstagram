package net.group3.quackstagram.backend.repositories;

import java.util.List;

import net.group3.quackstagram.models.User;

public interface UserRepository {
    User createUser(User user);

    User findUserByUsername(String username);

    List<User> findAllUsers();

    User updateUser(User user);

    void deleteUser(String username);

    List<User> findUsersFollowing(String username);

    List<User> findUsersFollowedBy(String username);

    // Additional methods for business-specific logic could include:
    boolean authenticateUser(String username, String password);

    void updatePassword(String username, String newPassword);

    void updateBio(String username, String newBio);

    void updateProfilePicture(String username, String newPicturePath);
}
