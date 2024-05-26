#!/bin/bash

# MySQL root credentials
MYSQL_ROOT_USER="root"
MYSQL_ROOT_PASSWORD="root_password"

# Application database and user
DB_NAME="quackstagram"
DB_USER="quack_user"
DB_PASSWORD="quack_password"

# SQL commands
SQL_COMMANDS=$(cat <<EOF
CREATE DATABASE IF NOT EXISTS $DB_NAME;
CREATE USER IF NOT EXISTS '$DB_USER'@'localhost' IDENTIFIED BY '$DB_PASSWORD';
GRANT ALL PRIVILEGES ON $DB_NAME.* TO '$DB_USER'@'localhost';
FLUSH PRIVILEGES;

USE $DB_NAME;

-- Create User table
CREATE TABLE IF NOT EXISTS User (
    userId BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    hashedPassword VARCHAR(255) NOT NULL,
    bio TEXT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Post table
CREATE TABLE IF NOT EXISTS Post (
    postId BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    imagePath TEXT NOT NULL,
    userId BIGINT NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES User(userId)
);

-- Create Comment table
CREATE TABLE IF NOT EXISTS Comment (
    commentId BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    userId BIGINT NOT NULL,
    postId BIGINT NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES User(userId),
    FOREIGN KEY (postId) REFERENCES Post(postId)
);

-- Create Like table
CREATE TABLE IF NOT EXISTS \`Like\` (
    likeId BIGINT AUTO_INCREMENT PRIMARY KEY,
    userId BIGINT NOT NULL,
    postId BIGINT NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES User(userId),
    FOREIGN KEY (postId) REFERENCES Post(postId)
);

-- Create Follow table
CREATE TABLE IF NOT EXISTS Follow (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    followerId BIGINT NOT NULL,
    followingId BIGINT NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (followerId) REFERENCES User(userId),
    FOREIGN KEY (followingId) REFERENCES User(userId)
);

-- Create Views
CREATE VIEW UserActivity AS
SELECT User.username, COUNT(Post.postId) AS postCount, COUNT(Comment.commentId) AS commentCount
FROM User
LEFT JOIN Post ON User.userId = Post.userId
LEFT JOIN Comment ON User.userId = Comment.userId
GROUP BY User.username;

CREATE VIEW PopularPosts AS
SELECT Post.postId, Post.content, COUNT(Like.likeId) AS likeCount
FROM Post
LEFT JOIN \`Like\` ON Post.postId = \`Like\`.postId
GROUP BY Post.postId
ORDER BY likeCount DESC;

CREATE VIEW SystemAnalytics AS
SELECT COUNT(User.userId) AS userCount, COUNT(Post.postId) AS postCount, COUNT(Comment.commentId) AS commentCount, COUNT(\`Like\`.likeId) AS likeCount, COUNT(Follow.id) AS followCount
FROM User, Post, Comment, \`Like\`, Follow;

-- Create Indexes
CREATE INDEX idx_user_username ON User(username);
CREATE INDEX idx_post_content ON Post(content(100));

-- Develop Procedures, Functions, and Triggers
DELIMITER //

CREATE PROCEDURE UpdatePostCount(IN userId BIGINT)
BEGIN
    UPDATE User
    SET postCount = (SELECT COUNT(*) FROM Post WHERE Post.userId = User.userId)
    WHERE User.userId = userId;
END //

CREATE FUNCTION GetUserPostCount(userId BIGINT) RETURNS INT
BEGIN
    DECLARE postCount INT;
    SELECT COUNT(*) INTO postCount FROM Post WHERE Post.userId = userId;
    RETURN postCount;
END //

CREATE TRIGGER after_insert_post
AFTER INSERT ON Post
FOR EACH ROW
BEGIN
    CALL UpdatePostCount(NEW.userId);
END //

CREATE TRIGGER before_insert_comment
BEFORE INSERT ON Comment
FOR EACH ROW
BEGIN
    SET NEW.createdAt = CURRENT_TIMESTAMP;
END //

DELIMITER ;
EOF
)

# Execute SQL commands
mysql -u$MYSQL_ROOT_USER -p$MYSQL_ROOT_PASSWORD -e "$SQL_COMMANDS"