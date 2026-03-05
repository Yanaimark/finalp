package com.yehoshua.finalp.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import com.yehoshua.finalp.datamodels.User;
import com.yehoshua.finalp.services.UserService;

@Route("register")
public class RegisterView extends VerticalLayout {

    private final UserService userService;

    public RegisterView(UserService userService) {

        this.userService = userService;

        TextField name = new TextField("Name");
        TextField email = new TextField("Email");

        Button registerButton = new Button("Register");

        registerButton.addClickListener(event -> {

            String userName = name.getValue();
            String userEmail = email.getValue();

            User user = new User(userName, userEmail);

            userService.insertUser(user);

            Notification.show("User Registered: " + userName);
        });

        add(name, email, registerButton);
    }
}