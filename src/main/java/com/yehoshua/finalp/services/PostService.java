package com.yehoshua.finalp.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yehoshua.finalp.datamodels.Post;
import com.yehoshua.finalp.repositories.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(Post post) {
        // Set the current date and time
        post.setCreatedAt(new Date());

        // Initialize likes to 0
        post.setLikesCount(0);

        // Save to MongoDB
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}