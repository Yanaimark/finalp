package com.yehoshua.finalp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yehoshua.finalp.datamodels.Post;
import com.yehoshua.finalp.datamodels.User;
import com.yehoshua.finalp.repositories.PostRepository;
import com.yehoshua.finalp.repositories.UserRepository;

@Service
public class FeedService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RecommendationService recommendationService;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPersonalizedFeed(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow();

        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .sorted((p1, p2) ->
                        Integer.compare(
                                recommendationService.calculateScore(p2, user),
                                recommendationService.calculateScore(p1, user)
                        ))
                .toList();
    }
}