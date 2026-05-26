package com.yehoshua.finalp.controllers;

import com.yehoshua.finalp.datamodels.User;
import com.yehoshua.finalp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.insertUser(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userService.login(
                user.getUsername(),
                user.getPassword()
        );
    }
}