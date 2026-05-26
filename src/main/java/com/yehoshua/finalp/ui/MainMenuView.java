package com.yehoshua.finalp.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("menu")
public class MainMenuView extends VerticalLayout {

    public MainMenuView() {

        boolean loggedIn = SessionManager.isLoggedIn();

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(true);
        setPadding(true);

        getStyle()
                .set("background", "linear-gradient(135deg, #1e3c72 0%, #2a5298 100%)");

        VerticalLayout card = new VerticalLayout();
        card.setWidth("450px");
        card.setAlignItems(Alignment.CENTER);
        card.setSpacing(true);
        card.setPadding(true);

        card.getStyle()
                .set("background-color", "white")
                .set("border-radius", "20px")
                .set("padding", "40px")
                .set("box-shadow", "0 10px 30px rgba(0,0,0,0.25)");

        H1 title = new H1("Main Menu");
        title.getStyle()
                .set("color", "#1e3c72")
                .set("margin-bottom", "10px");

        Paragraph roleText = new Paragraph("Current role: " + SessionManager.getRole());
        roleText.getStyle()
                .set("font-weight", "bold")
                .set("color", loggedIn ? "#27ae60" : "#e67e22");

        Button homeButton = createMenuButton("🏠 Home", "", "#34495e");
        Button feedButton = createMenuButton(
                loggedIn ? "🖼 View Personalized Feed" : "🖼 View Public Feed",
                "feed",
                "#3498db"
        );

        card.add(title, roleText, homeButton, feedButton);

        if (loggedIn) {
            Button createPostButton = createMenuButton("➕ Create New Post", "create-post", "#27ae60");
            Button profileButton = createMenuButton("👤 View Profile", "profile", "#9b59b6");

            Button logoutButton = new Button("🚪 Logout");
            logoutButton.setWidth("300px");
            logoutButton.getStyle()
                    .set("background-color", "#e74c3c")
                    .set("color", "white")
                    .set("font-size", "16px")
                    .set("font-weight", "bold")
                    .set("border-radius", "10px")
                    .set("padding", "12px");

            logoutButton.addClickListener(event -> {
                SessionManager.logout();
                Notification.show("Logged out. You are now a guest.");
                getUI().ifPresent(ui -> ui.navigate(""));
            });

            card.add(createPostButton, profileButton, logoutButton);
        } else {
            Button loginButton = createMenuButton("Login", "login", "#3498db");
            Button registerButton = createMenuButton("Create Account", "register", "#27ae60");

            card.add(loginButton, registerButton);
        }

        add(card);
    }

    private Button createMenuButton(String text, String route, String color) {

        Button button = new Button(text);
        button.setWidth("300px");

        button.getStyle()
                .set("background-color", color)
                .set("color", "white")
                .set("font-size", "16px")
                .set("font-weight", "bold")
                .set("border-radius", "10px")
                .set("padding", "12px");

        button.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate(route)));

        return button;
    }
}