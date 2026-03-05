package com.yehoshua.finalp.ui;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("login")
public class LoginView extends VerticalLayout {

    public LoginView() {

        LoginForm loginForm = new LoginForm();

        loginForm.addLoginListener(event -> {
            String username = event.getUsername();
            String password = event.getPassword();

            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
        });

        add(loginForm);
    }
}