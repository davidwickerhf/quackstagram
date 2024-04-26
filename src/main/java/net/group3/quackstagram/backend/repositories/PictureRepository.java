package net.group3.quackstagram.backend.repositories;

import java.util.List;

import net.group3.quackstagram.models.Picture;

public interface PictureRepository {
    Picture createPicture(Picture picture);

    Picture findPictureById(int id);

    List<Picture> findAllPictures();

    List<Picture> findPicturesByUser(String username);

    Picture updatePicture(Picture picture);

    void deletePicture(int id);

    // Additional methods for business-specific logic could include:
    List<Picture> findRecentPictures(int limit);

    List<Picture> findPicturesWithMostLikes(int limit);

    void likePicture(int pictureId, String username);

    void unlikePicture(int pictureId, String username);

    int countLikes(int pictureId);
    // ... other methods as needed
}
