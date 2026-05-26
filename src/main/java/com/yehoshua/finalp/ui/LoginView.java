package com.yehoshua.finalp.ui;

import com.yehoshua.finalp.datamodels.User;
import com.yehoshua.finalp.services.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
public class LoginView extends VerticalLayout {

    @Autowired
    public LoginView(UserService userService) {

        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");

        Button loginButton = new Button("Login", event -> {
           User user = userService.login(usernameField.getValue().trim(), passwordField.getValue().trim());
            if (user != null) {
                SessionManager.setCurrentUser(user);
                Notification.show("Login successful!");
                getUI().ifPresent(ui -> ui.navigate("feed"));
                SessionManager.setCurrentUser(user);
                Notification.show("Logged in as USER");
                getUI().ifPresent(ui -> ui.navigate("feed"));
            } else {
                Notification.show("Invalid username or password.");
            }
        });

        add(
                usernameField,
                passwordField,
                loginButton
        );
    }
}