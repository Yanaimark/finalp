package com.yehoshua.finalp.controllers;

import com.yehoshua.finalp.datamodels.Post;
import com.yehoshua.finalp.services.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    @Autowired
    private FeedService feedService;

    @GetMapping("/{userId}")
    public List<Post> getFeed(@PathVariable String userId) {
        return feedService.getPersonalizedFeed(userId);
    }
}