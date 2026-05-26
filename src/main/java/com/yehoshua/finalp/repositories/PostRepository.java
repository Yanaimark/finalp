package com.yehoshua.finalp.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.yehoshua.finalp.datamodels.Post;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByTagsIn(List<String> tags);
}
