package com.yehoshua.finalp.datamodels;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "interactions")
public class Interaction {

    @Id
    private String id;

    private String userId;
    private String postId;
    private String type; // like, comment, save
    private Date createdAt;

    // Empty constructor
    public Interaction() {
    }

    // Constructor without id
    public Interaction(String userId, String postId,String type, Date createdAt) {
        this.userId = userId;
        this.postId = postId;
        this.type = type;
        this.createdAt = createdAt;
    }

    // Full constructor
    public Interaction(String id, String userId, String postId,String type, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.type = type;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getType() {
        return type;
    }

    // Example values: "like", "comment", "save"
    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
