package net.group3.quackstagram.backend.database;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.group3.quackstagram.models.Comment;
import net.group3.quackstagram.models.Follow;
import net.group3.quackstagram.models.Like;
import net.group3.quackstagram.models.Post;
import net.group3.quackstagram.models.User;

public class DataFacade {
    private HikariDataSource dataSource;

    public DataFacade(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    // User CRUD methods
    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO User (username, email, hashedPassword, bio, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getHashedPassword());
            statement.setString(4, user.getBio());
            statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            statement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public User findUser(Long userId) throws SQLException {
        String sql = "SELECT * FROM User WHERE userId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setUserId(resultSet.getLong("userId"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setHashedPassword(resultSet.getString("hashedPassword"));
                    user.setBio(resultSet.getString("bio"));
                    user.setCreatedAt(resultSet.getTimestamp("createdAt"));
                    user.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
                    return user;
                }
            }
        }
        return null;
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE User SET username = ?, email = ?, hashedPassword = ?, bio = ?, updatedAt = ? WHERE userId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getHashedPassword());
            statement.setString(4, user.getBio());
            statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            statement.setLong(6, user.getUserId());
            statement.executeUpdate();
        }
    }

    public void deleteUser(Long userId) throws SQLException {
        String sql = "DELETE FROM User WHERE userId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        }
    }

    public List<User> findAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getLong("userId"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setHashedPassword(resultSet.getString("hashedPassword"));
                user.setBio(resultSet.getString("bio"));
                user.setCreatedAt(resultSet.getTimestamp("createdAt"));
                user.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
                users.add(user);
            }
        }
        return users;
    }

    // Post CRUD methods
    public void createPost(Post post) throws SQLException {
        String sql = "INSERT INTO Post (content, imagePath, userId, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getContent());
            statement.setString(2, post.getImagePath());
            statement.setLong(3, post.getUserId());
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setPostId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public Post findPost(Long postId) throws SQLException {
        String sql = "SELECT * FROM Post WHERE postId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, postId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Post post = new Post();
                    post.setPostId(resultSet.getLong("postId"));
                    post.setContent(resultSet.getString("content"));
                    post.setImagePath(resultSet.getString("imagePath"));
                    post.setUserId(resultSet.getLong("userId"));
                    post.setCreatedAt(resultSet.getTimestamp("createdAt"));
                    post.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
                    return post;
                }
            }
        }
        return null;
    }

    public void updatePost(Post post) throws SQLException {
        String sql = "UPDATE Post SET content = ?, imagePath = ?, userId = ?, updatedAt = ? WHERE postId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, post.getContent());
            statement.setString(2, post.getImagePath());
            statement.setLong(3, post.getUserId());
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            statement.setLong(5, post.getPostId());
            statement.executeUpdate();
        }
    }

    public void deletePost(Long postId) throws SQLException {
        String sql = "DELETE FROM Post WHERE postId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, postId);
            statement.executeUpdate();
        }
    }

    public List<Post> findAllPosts() throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM Post";
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Post post = new Post();
                post.setPostId(resultSet.getLong("postId"));
                post.setContent(resultSet.getString("content"));
                post.setImagePath(resultSet.getString("imagePath"));
                post.setUserId(resultSet.getLong("userId"));
                post.setCreatedAt(resultSet.getTimestamp("createdAt"));
                post.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
                posts.add(post);
            }
        }
        return posts;
    }

    public List<Post> findPostsByUserId(Long userId) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM Post WHERE userId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Post post = new Post();
                    post.setPostId(resultSet.getLong("postId"));
                    post.setContent(resultSet.getString("content"));
                    post.setImagePath(resultSet.getString("imagePath"));
                    post.setUserId(resultSet.getLong("userId"));
                    post.setCreatedAt(resultSet.getTimestamp("createdAt"));
                    post.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
                    posts.add(post);
                }
            }
        }
        return posts;
    }

    // Comment CRUD methods
    public void createComment(Comment comment) throws SQLException {
        String sql = "INSERT INTO Comment (content, userId, postId, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, comment.getContent());
            statement.setLong(2, comment.getUserId());
            statement.setLong(3, comment.getPostId());
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    comment.setCommentId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public Comment findComment(Long commentId) throws SQLException {
        String sql = "SELECT * FROM Comment WHERE commentId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, commentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Comment comment = new Comment();
                    comment.setCommentId(resultSet.getLong("commentId"));
                    comment.setContent(resultSet.getString("content"));
                    comment.setUserId(resultSet.getLong("userId"));
                    comment.setPostId(resultSet.getLong("postId"));
                    comment.setCreatedAt(resultSet.getTimestamp("createdAt"));
                    comment.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
                    return comment;
                }
            }
        }
        return null;
    }

    public void updateComment(Comment comment) throws SQLException {
        String sql = "UPDATE Comment SET content = ?, userId = ?, postId = ?, updatedAt = ? WHERE commentId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, comment.getContent());
            statement.setLong(2, comment.getUserId());
            statement.setLong(3, comment.getPostId());
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            statement.setLong(5, comment.getCommentId());
            statement.executeUpdate();
        }
    }

    public void deleteComment(Long commentId) throws SQLException {
        String sql = "DELETE FROM Comment WHERE commentId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, commentId);
            statement.executeUpdate();
        }
    }

    public List<Comment> findAllComments() throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM Comment";
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Comment comment = new Comment();
                comment.setCommentId(resultSet.getLong("commentId"));
                comment.setContent(resultSet.getString("content"));
                comment.setUserId(resultSet.getLong("userId"));
                comment.setPostId(resultSet.getLong("postId"));
                comment.setCreatedAt(resultSet.getTimestamp("createdAt"));
                comment.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
                comments.add(comment);
            }
        }
        return comments;
    }

    // Like CRUD methods
    public void createLike(Like like) throws SQLException {
        String sql = "INSERT INTO `Like` (userId, postId, createdAt) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, like.getUserId());
            statement.setLong(2, like.getPostId());
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    like.setLikeId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public Like findLike(Long likeId) throws SQLException {
        String sql = "SELECT * FROM `Like` WHERE likeId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, likeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Like like = new Like();
                    like.setLikeId(resultSet.getLong("likeId"));
                    like.setUserId(resultSet.getLong("userId"));
                    like.setPostId(resultSet.getLong("postId"));
                    like.setCreatedAt(resultSet.getTimestamp("createdAt"));
                    return like;
                }
            }
        }
        return null;
    }

    public void updateLike(Like like) throws SQLException {
        String sql = "UPDATE `Like` SET userId = ?, postId = ?, updatedAt = ? WHERE likeId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, like.getUserId());
            statement.setLong(2, like.getPostId());
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setLong(4, like.getLikeId());
            statement.executeUpdate();
        }
    }

    public void deleteLike(Long likeId) throws SQLException {
        String sql = "DELETE FROM `Like` WHERE likeId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, likeId);
            statement.executeUpdate();
        }
    }

    public List<Like> findAllLikes() throws SQLException {
        List<Like> likes = new ArrayList<>();
        String sql = "SELECT * FROM `Like`";
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Like like = new Like();
                like.setLikeId(resultSet.getLong("likeId"));
                like.setUserId(resultSet.getLong("userId"));
                like.setPostId(resultSet.getLong("postId"));
                like.setCreatedAt(resultSet.getTimestamp("createdAt"));
                likes.add(like);
            }
        }
        return likes;
    }

    // Follow CRUD methods
    public void createFollow(Follow follow) throws SQLException {
        String sql = "INSERT INTO Follow (followerId, followingId, createdAt) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, follow.getFollowerId());
            statement.setLong(2, follow.getFollowingId());
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    follow.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public Follow findFollow(Long id) throws SQLException {
        String sql = "SELECT * FROM Follow WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Follow follow = new Follow();
                    follow.setId(resultSet.getLong("id"));
                    follow.setFollowerId(resultSet.getLong("followerId"));
                    follow.setFollowingId(resultSet.getLong("followingId"));
                    follow.setCreatedAt(resultSet.getTimestamp("createdAt"));
                    return follow;
                }
            }
        }
        return null;
    }

    public void updateFollow(Follow follow) throws SQLException {
        String sql = "UPDATE Follow SET followerId = ?, followingId = ?, updatedAt = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, follow.getFollowerId());
            statement.setLong(2, follow.getFollowingId());
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setLong(4, follow.getId());
            statement.executeUpdate();
        }
    }

    public void deleteFollow(Long id) throws SQLException {
        String sql = "DELETE FROM Follow WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    public List<Follow> findAllFollows() throws SQLException {
        List<Follow> follows = new ArrayList<>();
        String sql = "SELECT * FROM Follow";
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Follow follow = new Follow();
                follow.setId(resultSet.getLong("id"));
                follow.setFollowerId(resultSet.getLong("followerId"));
                follow.setFollowingId(resultSet.getLong("followingId"));
                follow.setCreatedAt(resultSet.getTimestamp("createdAt"));
                follows.add(follow);
            }
        }
        return follows;
    }

    public int getFollowersCount(Long userId) throws SQLException {
        String sql = "SELECT COUNT(*) AS followersCount FROM Follow WHERE followingId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("followersCount");
                }
            }
        }
        return 0;
    }

    public int getFollowingCount(Long userId) throws SQLException {
        String sql = "SELECT COUNT(*) AS followingCount FROM Follow WHERE followerId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("followingCount");
                }
            }
        }
        return 0;
    }

    public boolean isFollowing(Long followerId, Long followingId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM Follow WHERE followerId = ? AND followingId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, followerId);
            statement.setLong(2, followingId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count") > 0;
                }
            }
        }
        return false;
    }

    public void followUser(Long followerId, Long followingId) throws SQLException {
        if (!isFollowing(followerId, followingId)) {
            Follow follow = new Follow();
            follow.setFollowerId(followerId);
            follow.setFollowingId(followingId);
            createFollow(follow);
        }
    }

    // Method to get the total number of likes for a post
    public int getPostLikeCount(Long postId) throws SQLException {
        String sql = "SELECT COUNT(*) AS likeCount FROM `Like` WHERE postId = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, postId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("likeCount");
                }
            }
        }
        return 0;
    }
}
