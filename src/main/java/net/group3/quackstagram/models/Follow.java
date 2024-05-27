package net.group3.quackstagram.models;

import java.sql.Timestamp;

public class Follow {
    private Long id;
    private Long followerId;
    private Long followingId;
    private Timestamp createdAt;

    // Getters
    public Long getId() {
        return id;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
