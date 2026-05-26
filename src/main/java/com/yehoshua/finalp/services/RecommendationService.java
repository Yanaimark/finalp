package com.yehoshua.finalp.services;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.yehoshua.finalp.datamodels.Post;
import com.yehoshua.finalp.datamodels.User;

@Service
public class RecommendationService {

    public int calculateScore(Post post, User user) {

        // Safety checks
        if (post.getTags() == null ||
            user.getPreferenceVector() == null) {
            return 0;
        }

        int score = 0;
        Map<String, Integer> preferenceVector =
                user.getPreferenceVector();

        // Sum weights of matching tags
        for (String tag : post.getTags()) {
            Integer weight = preferenceVector.get(tag);
            if (weight != null) {
                score += weight;
            }
        }

        return score;
    }
}