package com.yehoshua.finalp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yehoshua.finalp.datamodels.User;
import com.yehoshua.finalp.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Insert user
    public User insertUser(User user) {
        return userRepository.save(user);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by id
    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    // Update user
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // Delete user
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}