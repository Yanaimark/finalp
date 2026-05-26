package com.yehoshua.finalp.datamodels;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String username;
    private String email;
    private String password;
    private Date createdAt;

    // Original interests list (still useful during registration)
    private List<String> interests;

    // Weighted preference vector: tag -> weight
    private Map<String, Integer> preferenceVector;

    // IDs of recently liked posts
    private List<String> likedPostIds;

    public User() {
        this.createdAt = new Date();
        this.interests = new ArrayList<>();
        this.preferenceVector = new HashMap<>();
        this.likedPostIds = new ArrayList<>();
    }

    public User(String username, String email, String password) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // ---------- Getters and Setters ----------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public Map<String, Integer> getPreferenceVector() {
        return preferenceVector;
    }

    public void setPreferenceVector(Map<String, Integer> preferenceVector) {
        this.preferenceVector = preferenceVector;
    }

    public List<String> getLikedPostIds() {
        return likedPostIds;
    }

    public void setLikedPostIds(List<String> likedPostIds) {
        this.likedPostIds = likedPostIds;
    }
}