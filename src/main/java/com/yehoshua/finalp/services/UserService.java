package com.yehoshua.finalp.services;

import com.yehoshua.finalp.datamodels.Post;
import com.yehoshua.finalp.datamodels.User;
import com.yehoshua.finalp.repositories.PostRepository;
import com.yehoshua.finalp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UserService {

    private static final int MAX_RECENT_LIKES = 20;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    // Register / Insert User
    public User insertUser(User user) {
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(new Date());
        }

        if (user.getInterests() == null) {
            user.setInterests(new ArrayList<>());
        }

        if (user.getPreferenceVector() == null) {
            user.setPreferenceVector(new HashMap<>());
        }

        if (user.getLikedPostIds() == null) {
            user.setLikedPostIds(new ArrayList<>());
        }

        // Initialize preference vector from interests
        for (String interest : user.getInterests()) {
            user.getPreferenceVector().put(interest, 1);
        }

        return userRepository.save(user);
    }

    public User register(User user) {
        return insertUser(user);
    }

    // Login
    public User login(String username, String password) {
        Optional<User> optionalUser =
                userRepository.findByUsername(username.trim());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPassword() != null &&
                user.getPassword().trim().equals(password.trim())) {
                return user;
            }
        }

        return null;
    }

    // Like a post
    public User likePost(String userId, String postId) {
        User user = userRepository.findById(userId)
                .orElseThrow();

        Post post = postRepository.findById(postId)
                .orElseThrow();

        // Initialize list if needed
        if (user.getLikedPostIds() == null) {
            user.setLikedPostIds(new ArrayList<>());
        }

        // Prevent duplicate likes
        if (user.getLikedPostIds().contains(postId)) {
            return user;
        }

        // Add post to liked posts
        user.getLikedPostIds().add(postId);

        // Keep only the most recent MAX_RECENT_LIKES
        while (user.getLikedPostIds().size() > MAX_RECENT_LIKES) {
            String removedPostId = user.getLikedPostIds().remove(0);

            // Decrease likes count for removed post
            postRepository.findById(removedPostId).ifPresent(oldPost -> {
                if (oldPost.getLikesCount() > 0) {
                    oldPost.setLikesCount(oldPost.getLikesCount() - 1);
                    postRepository.save(oldPost);
                }
            });
        }

        // Increase likes count
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);

        // Rebuild preference vector
        rebuildPreferenceVector(user);

        // Save and return updated user
        return userRepository.save(user);
    }

    // Unlike a post
    public User unlikePost(String userId, String postId) {
        User user = userRepository.findById(userId)
                .orElseThrow();

        Post post = postRepository.findById(postId)
                .orElseThrow();

        // Initialize list if needed
        if (user.getLikedPostIds() == null) {
            user.setLikedPostIds(new ArrayList<>());
        }

        // Remove from liked posts
        if (user.getLikedPostIds().remove(postId)) {

            // Decrease likes count
            if (post.getLikesCount() > 0) {
                post.setLikesCount(post.getLikesCount() - 1);
                postRepository.save(post);
            }

            // Rebuild preference vector
            rebuildPreferenceVector(user);

            // Save and return updated user
            return userRepository.save(user);
        }

        // Nothing changed
        return user;
    }

    // Recalculate preference vector
    private void rebuildPreferenceVector(User user) {
        HashMap<String, Integer> vector = new HashMap<>();

        // Start with initial interests
        if (user.getInterests() != null) {
            for (String interest : user.getInterests()) {
                vector.put(interest, vector.getOrDefault(interest, 0) + 1);
            }
        }

        // Add weights from liked posts
        if (user.getLikedPostIds() != null) {
            for (String postId : user.getLikedPostIds()) {
                Optional<Post> optionalPost =
                        postRepository.findById(postId);

                if (optionalPost.isPresent()) {
                    Post post = optionalPost.get();

                    if (post.getTags() != null) {
                        for (String tag : post.getTags()) {
                            vector.put(tag,
                                    vector.getOrDefault(tag, 0) + 1);
                        }
                    }
                }
            }
        }

        user.setPreferenceVector(vector);
    }
}