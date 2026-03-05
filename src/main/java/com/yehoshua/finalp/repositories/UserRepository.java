package com.yehoshua.finalp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.yehoshua.finalp.datamodels.User;

public interface UserRepository extends MongoRepository<User, String> {

}