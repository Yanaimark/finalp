package com.yehoshua.finalp.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class NavBar extends HorizontalLayout {

    public NavBar() {

        boolean loggedIn = SessionManager.isLoggedIn();

        setWidthFull();
        setSpacing(true);
        setPadding(true);

        getStyle()
                .set("background-color", "#2c3e50")
                .set("padding", "15px")
                .set("border-radius", "10px")
                .set("margin-bottom", "20px");

        Button homeButton = createButton("🏠 Home", null);
        Button feedButton = createButton("🖼 Feed", "feed");

        add(homeButton, feedButton);

        if (loggedIn) {
            add(
                    createButton("➕ Create Post", "create-post"),
                    createButton("👤 Profile", "profile"),
                    createButton("📋 Menu", "menu")
            );

            Button logoutButton = new Button("🚪 Logout");
            logoutButton.getStyle()
                    .set("background-color", "#e74c3c")
                    .set("color", "white")
                    .set("font-weight", "bold");

            logoutButton.addClickListener(event -> {
                SessionManager.logout();
                getUI().ifPresent(ui -> ui.navigate(HomeView.class));
            });

            add(logoutButton);

        } else {
            add(
                    createButton("Login", "login"),
                    createButton("Register", "register")
            );
        }

        Paragraph roleText = new Paragraph("Role: " + SessionManager.getRole());
        roleText.getStyle()
                .set("color", "white")
                .set("font-weight", "bold")
                .set("margin-left", "auto");

        add(roleText);
    }

    private Button createButton(String text, String route) {

        Button button = new Button(text);

        button.getStyle()
                .set("background-color", "#3498db")
                .set("color", "white")
                .set("font-weight", "bold")
                .set("border-radius", "8px");

        button.addClickListener(event -> {
            if (route == null || route.isEmpty()) {
                getUI().ifPresent(ui -> ui.navigate(HomeView.class));
            } else {
                getUI().ifPresent(ui -> ui.navigate(route));
            }
        });

        return button;
    }
}