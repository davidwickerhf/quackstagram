package net.group3.quackstagram.models;

import java.sql.Timestamp;

public class Like {
    private Long likeId;
    private Long userId;
    private Long postId;
    private Timestamp createdAt;

    // Getters
    public Long getLikeId() {
        return likeId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPostId() {
        return postId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
