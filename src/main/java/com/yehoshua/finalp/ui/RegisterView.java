package com.yehoshua.finalp.ui;

import com.yehoshua.finalp.datamodels.User;
import com.yehoshua.finalp.services.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("register")
public class RegisterView extends VerticalLayout {

    @Autowired
    public RegisterView(UserService userService) {

        TextField usernameField = new TextField("Username");
        EmailField emailField = new EmailField("Email");
        PasswordField passwordField = new PasswordField("Password");

        Button registerButton = new Button("Register", event -> {
            User user = new User(
                    usernameField.getValue(),
                    emailField.getValue(),
                    passwordField.getValue()
            );

            User savedUser = userService.insertUser(user);

            Notification.show(
                    "User registered successfully! Username: "
                            + savedUser.getUsername()
            );

            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        add(
                usernameField,
                emailField,
                passwordField,
                registerButton
        );
    }
}