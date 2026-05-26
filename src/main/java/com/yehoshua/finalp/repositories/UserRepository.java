package com.yehoshua.finalp.repositories;

import com.yehoshua.finalp.datamodels.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}