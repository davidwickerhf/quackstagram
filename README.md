# Quackstagram Project Documentation

## Running the Application

### Prerequisites

Here are the commands you need to install the required software for different platforms:

#### Install Java Development Kit (JDK)

**Windows**:

1. Download and install from Oracle's website manually or use Chocolatey:
   ```sh
   choco install jdk11 -y
   ```

**Mac**:

1. Using Homebrew:
   ```sh
   brew install openjdk@11
   ```

**Linux**:

1. Using APT (Ubuntu/Debian):
   ```sh
   sudo apt update && sudo apt install openjdk-11-jdk -y
   ```
2. Using YUM (CentOS/RHEL):
   ```sh
   sudo yum install java-11-openjdk-devel -y
   ```

#### Install MySQL

**Windows**:

1. Download and install from MySQL's website manually or use Chocolatey:
   ```sh
   choco install mysql -y
   ```
2. Start MySQL service:
   ```sh
   net start mysql
   ```

**Mac**:

1. Using Homebrew:
   ```sh
   brew install mysql
   ```
2. Start MySQL service:
   ```sh
   brew services start mysql
   ```

**Linux**:

1. Using APT (Ubuntu/Debian):
   ```sh
   sudo apt update && sudo apt install mysql-server -y
   ```
2. Using YUM (CentOS/RHEL):
   ```sh
   sudo yum install mysql-server -y
   ```
3. Start MySQL service:
   ```sh
   sudo service mysql start
   ```

#### Install Gradle

**Windows**:

1. Download and install from Gradle's website manually or use Chocolatey:
   ```sh
   choco install gradle -y
   ```

**Mac**:

1. Using Homebrew:
   ```sh
   brew install gradle
   ```

**Linux**:

1. Using SDKMAN:
   ```sh
   curl -s "https://get.sdkman.io" | bash
   source "$HOME/.sdkman/bin/sdkman-init.sh"
   sdk install gradle
   ```

These commands should help users quickly set up the required environment for the project on their respective platforms.

### Database Setup

2. **Make the script executable**:

   ```sh
   chmod +x setup_mysql.sh
   ```

3. **Run the script**:
   ```sh
   ./setup_mysql.sh
   ```

This script will start the mysql service in the background, and set up the MySQL database, user, schema, views, indexes, procedures, functions, and triggers.

### Application Setup

1. **Clone the repository**:

   ```sh
   git clone https://github.com/your-username/quackstagram.git
   cd quackstagram
   ```

2. **Build the project**:

   ```sh
   gradle build
   ```

3. **Run the application**:
   ```sh
   gradle run
   ```

## Java Models

### User Model

```java
public class User {
    private Long userId;
    private String username;
    private String email;
    private String hashedPassword;
    private String bio;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Getters and Setters
}
```

### Post Model

```java
public class Post {
    private Long postId;
    private String content;
    private String imagePath;
    private Long userId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Getters and Setters
}
```

### Comment Model

```java
public class Comment {
    private Long commentId;
    private String content;
    private Long userId;
    private Long postId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Getters and Setters
}
```

### Like Model

```java
public class Like {
    private Long likeId;
    private Long userId;
    private Long postId;
    private Timestamp createdAt;

    // Getters and Setters
}
```

### Follow Model

```java
public class Follow {
    private Long id;
    private Long followerId;
    private Long followingId;
    private Timestamp createdAt;

    // Getters and Setters
}
```

## MySQL Schema

```sql
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
CREATE TABLE IF NOT EXISTS `Like` (
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

--Create Views
CREATE VIEW UserActivity AS
SELECT User.username, COUNT(Post.postId) AS postCount, COUNT(Comment.commentId) AS commentCount
FROM User
LEFT JOIN Post ON User.userId = Post.userId
LEFT JOIN Comment ON User.userId = Comment.userId
GROUP BY User.username;

CREATE VIEW PopularPosts AS
SELECT Post.postId, Post.content, COUNT(`Like`.likeId) AS likeCount
FROM Post
LEFT JOIN `Like` ON Post.postId = `Like`.postId
GROUP BY Post.postId
ORDER BY likeCount DESC;

CREATE VIEW SystemAnalytics AS
SELECT COUNT(User.userId) AS userCount, COUNT(Post.postId) AS postCount, COUNT(Comment.commentId) AS commentCount, COUNT(`Like`.likeId) AS likeCount, COUNT(Follow.id) AS followCount
FROM User, Post, Comment, `Like`, Follow;

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
```

## Database Connection

### DatabaseConnection Class

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/quackstagram";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
```

## Data Access Object (DAO)

### UserDAO Class

```java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;

    public UserDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    // Example method, view UserDAO.java
    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO User (username, email, hashedPassword, bio, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
}
```

### Example Usage

```java
public class Main {
    public static void main(String[] args) {
        try {
            UserDAO userDAO = new UserDAO();

            // Create a new user
            User user = new User();
            user.setUsername("john_doe");
            user.setEmail("john@example.com");
            user.setHashedPassword("hashed_password");
            user.setBio("Just a regular user.");

            userDAO.createUser(user);

            // Find the user by ID
            User foundUser = userDAO.findUser(user.getUserId());
            System.out.println("Found user: " + foundUser.getUsername());

            // Update the user
            foundUser.setBio("Updated bio.");
            userDAO.updateUser(foundUser);

            // Delete the user
            userDAO.deleteUser(foundUser.getUserId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

## Proof of 3NF

All tables (User, Post, Comment, Like, Follow) are in 3NF because:

- Each table is already in 1NF (all attributes are atomic).
- Each table is in 2NF (all non-key attributes are fully functionally dependent on the whole primary key).
- Each table is in 3NF (there are no transitive dependencies, i.e., non-key attributes are not dependent on other non-key attributes).

## ERD Diagram

### Rendered ERD

![ERD](/schema.png)

### PlantUML Code

```plantuml
@startuml
entity User {
    + userId : BIGINT <<PK>>
    --
    username : VARCHAR(255)
    email : VARCHAR(255)
    hashedPassword : VARCHAR(255)
    bio : TEXT
    createdAt : TIMESTAMP
    updatedAt : TIMESTAMP
}

entity Post {
    + postId : BIGINT <<PK>>
    --
    content : TEXT
    imagePath : TEXT
    userId : BIGINT <<FK>>
    createdAt : TIMESTAMP
    updatedAt : TIMESTAMP
}

entity Comment {
    + commentId : BIGINT <<PK>>
    --
    content : TEXT
    userId : BIGINT <<FK>>
    postId : BIGINT <<FK>>
    createdAt : TIMESTAMP
    updatedAt : TIMESTAMP
}

entity Like {
    + likeId : BIGINT <<PK>>
    --
    userId : BIGINT <<FK>>
    postId : BIGINT <<FK>>
    createdAt : TIMESTAMP
}

entity Follow {
    + id : BIGINT <<PK>>
    --
    followerId : BIGINT <<FK>>
    followingId : BIGINT <<FK>>
    createdAt : TIMESTAMP
}

User ||--o{ Post : "has"
User ||--o{ Comment : "makes"
User ||--o{ Like : "likes"
User ||--o{ Follow : "follows"
Post ||--o{ Comment : "has"
Post ||--o{ Like : "is liked"
@enduml
```

## Profit Maximization Queries

1. **List all users who have more than X followers where X can be any integer value.**

   ```sql
   SELECT u.username, COUNT(f.followerId) AS follower_count
   FROM User u
   JOIN Follow f ON u.userId = f.followingId
   GROUP BY u.userId
   HAVING COUNT(f.followerId) > X;
   ```

2. **Show the total number of posts made by each user.**

   ```sql
   SELECT u.username, COUNT(p.postId) AS total_posts
   FROM User u
   LEFT JOIN Post p ON u.userId = p.userId
   GROUP BY u.userId;
   ```

3. **Find all comments made on a particular user’s post.**

   ```sql
   SELECT c.*
   FROM Comment c
   JOIN Post p ON c.postId = p.postId
   WHERE p.userId = (SELECT userId FROM User WHERE username = 'particular_username');
   ```

4. **Display the top X most liked posts.**

   ```sql
   SELECT p.*, COUNT(l.likeId) AS like_count
   FROM Post p
   LEFT JOIN `Like` l ON p.postId = l.postId
   GROUP BY p.postId
   ORDER BY like_count DESC
   LIMIT X;
   ```

5. **Count the number of posts each user has liked.**

   ```sql
   SELECT u.username, COUNT(l.likeId) AS total_likes
   FROM User u
   LEFT JOIN `Like` l ON u.userId = l.userId
   GROUP BY u.userId;
   ```

6. **List all users who haven’t made a post yet.**

   ```sql
   SELECT u.username
   FROM User u
   LEFT JOIN Post p ON u.userId = p.userId
   WHERE p.postId IS NULL;
   ```

7. **List users who follow each other.**

   ```sql
   SELECT u1.username AS user1, u2.username AS user2
   FROM Follow f1
   JOIN Follow f2 ON f1.followerId = f2.followingId AND f1.followingId = f2.followerId
   JOIN User u1 ON f1.followerId = u1.userId
   JOIN User u2 ON f1.followingId = u2.userId;
   ```

8. **Show the user with the highest number of posts.**

   ```sql
   SELECT u.username, COUNT(p.postId) AS total_posts
   FROM User u
   LEFT JOIN Post p ON u.userId = p.userId
   GROUP BY u.userId
   ORDER BY total_posts DESC
   LIMIT 1;
   ```

9. **List the top X users with the most followers.**

   ```sql
   SELECT u.username, COUNT(f.followerId) AS follower_count
   FROM User u
   LEFT JOIN Follow f ON u.userId = f.followingId
   GROUP BY u.userId
   ORDER BY follower_count DESC
   LIMIT X;
   ```

10. **Find posts that have been liked by all users.**

    ```sql
    SELECT p.*
    FROM Post p
    JOIN `Like` l ON p.postId = l.postId
    GROUP BY p.postId
    HAVING COUNT(DISTINCT l.userId) = (SELECT COUNT(*) FROM User);
    ```

11. **Display the most active user (based on posts, comments, and likes).**

    ```sql
    SELECT u.username,
           (COUNT(p.postId) + COUNT(c.commentId) + COUNT(l.likeId)) AS activity_count
    FROM User u
    LEFT JOIN Post p ON u.userId = p.userId
    LEFT JOIN Comment c ON u.userId = c.userId
    LEFT JOIN `Like` l ON u.userId = l.userId
    GROUP BY u.userId
    ORDER BY activity_count DESC
    LIMIT 1;
    ```

12. **Find the average number of likes per post for each user.**

    ```sql
    SELECT u.username, AVG(like_count) AS avg_likes_per_post
    FROM (
        SELECT p.userId, COUNT(l.likeId) AS like_count
        FROM Post p
        LEFT JOIN `Like` l ON p.postId = l.postId
        GROUP BY p.postId
    ) AS post_likes
    JOIN User u ON post_likes.userId = u.userId
    GROUP BY u.userId;
    ```

13. **Show posts that have more comments than likes.**

    ```sql
    SELECT p.*
    FROM Post p
    LEFT JOIN Comment c ON p.postId = c.postId
    LEFT JOIN `Like` l ON p.postId = l.postId
    GROUP BY p.postId
    HAVING COUNT(c.commentId) > COUNT(l.likeId);
    ```

14. **List the users who have liked every post of a specific user.**

    ```sql
    SELECT u.username
    FROM User u
    WHERE NOT EXISTS (
        SELECT p.postId
        FROM Post p
        WHERE p.userId = (SELECT userId FROM User WHERE username = 'specific_username')
        AND NOT EXISTS (
            SELECT l.likeId
            FROM `Like` l
            WHERE l.postId = p.postId AND l.userId = u.userId
        )
    );
    ```

15. **Display the most popular post of each user (based on likes).**

    ```sql
    SELECT p1.*
    FROM Post p1
    LEFT JOIN `Like` l1 ON p1.postId = l1.postId
    LEFT JOIN (
        SELECT p.userId, MAX(like_count) AS max_likes
        FROM (
            SELECT p.userId, p.postId, COUNT(l.likeId) AS like_count
            FROM Post p
            LEFT JOIN `Like` l ON p.postId = l.postId
            GROUP BY p.userId, p.postId
        ) AS user_posts
        GROUP BY user_posts.userId
    ) AS max_likes_per_user ON p1.userId = max_likes_per_user.userId
                           AND COUNT(l1.likeId) = max_likes_per_user.max_likes
    GROUP BY p1.userId;
    ```

16. **Find the user(s) with the highest ratio of followers to following.**

    ```sql
    SELECT u.username,
           (follower_count / following_count) AS follower_following_ratio
    FROM User u
    JOIN (
        SELECT u.userId,
               COUNT(DISTINCT f1.followerId) AS follower_count,
               COUNT(DISTINCT f2.followingId) AS following_count
        FROM User u
        LEFT JOIN Follow f1 ON u.userId = f1.followingId
        LEFT JOIN Follow f2 ON u.userId = f2.followerId
        GROUP BY u.userId
    ) AS counts ON u.userId = counts.userId
    ORDER BY follower_following_ratio DESC
    LIMIT 1;
    ```

17. **Show the month with the highest number of posts made.**

    ```sql
    SELECT DATE_FORMAT(createdAt, '%Y-%m') AS month, COUNT(postId) AS post_count
    FROM Post
    GROUP BY month
    ORDER BY post_count DESC
    LIMIT 1;
    ```

18. **Identify users who have not interacted with a specific user’s posts.**

    ```sql
    SELECT u.username
    FROM User u
    WHERE u.userId NOT IN (
        SELECT l.userId
        FROM `Like` l
        JOIN Post p ON l.postId = p.postId
        WHERE p.userId = (SELECT userId FROM User WHERE username = 'specific_username')
    )
    AND u.userId NOT IN (
        SELECT c.userId
        FROM Comment c
        JOIN Post p ON c.postId = p.postId
        WHERE p.userId = (SELECT userId FROM User WHERE username = 'specific_username')
    );
    ```

19. **Display the user with the greatest increase in followers in the last X days.**

    ```sql
    SELECT u.username, (COUNT(f1.followerId) - COUNT(f2.followerId)) AS follower_increase
    FROM User u
    LEFT JOIN Follow f1 ON u.userId = f1.followingId AND f1.createdAt > (CURRENT_DATE - INTERVAL X DAY)
    LEFT JOIN Follow f2 ON u.userId = f2.followingId AND f2.createdAt <= (CURRENT_DATE - INTERVAL X DAY)
    GROUP BY u.userId
    ORDER BY follower_increase DESC
    LIMIT 1;
    ```

20. **Find users who are followed by more than X% of the platform users.**
    ```sql
    SELECT u.username
    FROM User u
    JOIN Follow f ON u.userId = f.followingId
    GROUP BY u.userId
    HAVING (COUNT(f.followerId) / (SELECT COUNT(*) FROM User)) * 100 > X;
    ```

Adjust the `X` values and `specific_username` to test on specific values.
