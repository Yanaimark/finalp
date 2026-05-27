package com.yehoshua.finalp.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.yehoshua.finalp.datamodels.User;
import com.yehoshua.finalp.services.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("register")
public class RegisterView extends VerticalLayout {

    @Autowired
    public RegisterView(UserService userService) {

        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);

        add(new NavBar());

        H1 title = new H1("Create Account");

        TextField usernameField = new TextField("Username");
        usernameField.setWidth("350px");

        EmailField emailField = new EmailField("Email");
        emailField.setWidth("350px");

        PasswordField passwordField = new PasswordField("Password");
        passwordField.setWidth("350px");

        Button registerButton = new Button("Register");

        registerButton.getStyle()
                .set("background-color", "#27ae60")
                .set("color", "white")
                .set("font-weight", "bold")
                .set("border-radius", "8px");

        registerButton.addClickListener(event -> {

            String username = usernameField.getValue().trim();
            String email = emailField.getValue().trim();
            String password = passwordField.getValue().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Notification.show("Please fill all fields.");
                return;
            }

            User user = new User(username, email, password);

            User savedUser = userService.insertUser(user);

            Notification.show(
                    "User registered successfully! Username: "
                            + savedUser.getUsername()
            );

            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        add(
                title,
                usernameField,
                emailField,
                passwordField,
                registerButton
        );
    }
}