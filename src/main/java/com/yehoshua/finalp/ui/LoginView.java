package com.yehoshua.finalp.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.yehoshua.finalp.datamodels.User;
import com.yehoshua.finalp.services.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("login")
public class LoginView extends VerticalLayout {

    @Autowired
    public LoginView(UserService userService) {

        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);

        add(new NavBar());

        H1 title = new H1("Login");

        TextField usernameField = new TextField("Username");
        usernameField.setWidth("350px");

        PasswordField passwordField = new PasswordField("Password");
        passwordField.setWidth("350px");

        Button loginButton = new Button("Login");

        loginButton.getStyle()
                .set("background-color", "#3498db")
                .set("color", "white")
                .set("font-weight", "bold")
                .set("border-radius", "8px");

        loginButton.addClickListener(event -> {

            String username = usernameField.getValue().trim();
            String password = passwordField.getValue().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Notification.show("Please enter username and password.");
                return;
            }

            User user = userService.login(username, password);

            if (user != null) {
                SessionManager.setCurrentUser(user);
                Notification.show("Logged in as USER");
                getUI().ifPresent(ui -> ui.navigate("feed"));
            } else {
                Notification.show("Invalid username or password.");
            }
        });

        add(
                title,
                usernameField,
                passwordField,
                loginButton
        );
    }
}