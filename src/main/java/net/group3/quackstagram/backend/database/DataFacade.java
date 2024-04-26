package net.group3.quackstagram.backend.database;

import net.group3.quackstagram.backend.repositories.PictureRepository;
import net.group3.quackstagram.backend.repositories.SQLPictureRepository;
import net.group3.quackstagram.backend.repositories.SQLUserRepository;
import net.group3.quackstagram.backend.repositories.UserRepository;
import net.group3.quackstagram.models.Picture;
import net.group3.quackstagram.models.User;

public class DataFacade {
    private UserRepository userRepository;
    private PictureRepository pictureRepository;

    public DataFacade(DataSourceFactory dataSourceFactory) {
        this.userRepository = new SQLUserRepository(dataSourceFactory.getDataSource("database"));
        this.pictureRepository = new SQLPictureRepository(dataSourceFactory.getDataSource("database"));
    }

    // User actions
    public User getUserById(String id) {
        throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
    }

    public void saveUser(User user) {
        throw new UnsupportedOperationException("Unimplemented method 'saveUser'");
    }

    public Picture getPictureById(String id) {
        throw new UnsupportedOperationException("Unimplemented method 'getPictureById'");
    }

    public void savePicture(Picture picture) {
        throw new UnsupportedOperationException("Unimplemented method 'savePicture'");
    }

    // Posts actions
    public void likeImage(String username, String imageID) {
        throw new UnsupportedOperationException("Unimplemented method 'likeImage'");
    }

    public void unlikeImage(String username, String imageID) {
        throw new UnsupportedOperationException("Unimplemented method 'unlikeImage'");
    }

    public void getLikes(String imageID) {
        throw new UnsupportedOperationException("Unimplemented method 'getLikes'");
    }

    // Comments actions
    public void getComments(String imageID) {
        throw new UnsupportedOperationException("Unimplemented method 'getComments'");
    }

    public void addComment(String username, String imageID, String comment) {
        throw new UnsupportedOperationException("Unimplemented method 'addComment'");
    }

    // Relationships actions
    public void followUser(String follower, String followee) {
        throw new UnsupportedOperationException("Unimplemented method 'followUser'");
    }

    public void unfollowUser(String follower, String followee) {
        throw new UnsupportedOperationException("Unimplemented method 'unfollowUser'");
    }

    // Account actions

    public void deleteImage(String imageID) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteImage'");
    }

    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    public void updateBio(String username, String newBio) {
        throw new UnsupportedOperationException("Unimplemented method 'updateBio'");
    }

    public void updatePassword(String username, String newPassword) {
        throw new UnsupportedOperationException("Unimplemented method 'updatePassword'");
    }

    public void updateProfilePicture(String username, String newPicturePath) {
        throw new UnsupportedOperationException("Unimplemented method 'updateProfilePicture'");
    }

    // Counters
    public void countLikes(String imageID) {
        throw new UnsupportedOperationException("Unimplemented method 'countLikes'");
    }

    public void countFollowers(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'countFollowers'");
    }

    public void countFollowing(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'countFollowing'");
    }

    public void countPosts(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'countPosts'");
    }

    public void countComments(String imageID) {
        throw new UnsupportedOperationException("Unimplemented method 'countComments'");
    }
}