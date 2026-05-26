package com.yehoshua.finalp.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class HomeView extends VerticalLayout {

    public HomeView() {

        boolean loggedIn = SessionManager.isLoggedIn();

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(new NavBar());

        getStyle()
                .set("background", "linear-gradient(135deg, #1e3c72 0%, #2a5298 100%)")
                .set("padding", "40px");

        VerticalLayout card = new VerticalLayout();
        card.setWidth("600px");
        card.setAlignItems(Alignment.CENTER);
        card.setSpacing(true);
        card.setPadding(true);

        card.getStyle()
                .set("background-color", "white")
                .set("border-radius", "20px")
                .set("padding", "40px")
                .set("box-shadow", "0 10px 30px rgba(0,0,0,0.25)");

        Paragraph roleText = new Paragraph("Current role: " + SessionManager.getRole());
        roleText.getStyle()
                .set("color", loggedIn ? "#27ae60" : "#e67e22")
                .set("font-weight", "bold");

        H1 title = new H1("Gallery Miniatures");
        title.getStyle()
                .set("color", "#1e3c72")
                .set("font-size", "48px")
                .set("margin", "0");

        Paragraph subtitle = new Paragraph(
                "Share your miniatures, dioramas, and painting projects.");
        subtitle.getStyle()
                .set("color", "#666")
                .set("font-size", "18px")
                .set("text-align", "center")
                .set("margin-bottom", "20px");

        Image image = new Image("Picture2.png", "Miniatures and dioramas");
        image.setWidth("100%");
        image.setMaxWidth("500px");
        image.getStyle()
                .set("border-radius", "15px")
                .set("box-shadow", "0 4px 15px rgba(0,0,0,0.15)")
                .set("margin-bottom", "20px");

        Button feedButton = new Button("View Feed");
        feedButton.setWidth("220px");
        feedButton.getStyle()
                .set("background-color", "#8e44ad")
                .set("color", "white")
                .set("font-size", "18px")
                .set("font-weight", "bold")
                .set("border-radius", "10px")
                .set("padding", "12px");

        feedButton.addClickListener(event ->
                UI.getCurrent().navigate("feed"));

        Button registerButton = new Button("Create Account");
        registerButton.setWidth("220px");
        registerButton.getStyle()
                .set("background-color", "#27ae60")
                .set("color", "white")
                .set("font-size", "18px")
                .set("font-weight", "bold")
                .set("border-radius", "10px")
                .set("padding", "12px");

        registerButton.addClickListener(event ->
                UI.getCurrent().navigate("register"));

        Button loginButton = new Button("Login");
        loginButton.setWidth("220px");
        loginButton.getStyle()
                .set("background-color", "#3498db")
                .set("color", "white")
                .set("font-size", "18px")
                .set("font-weight", "bold")
                .set("border-radius", "10px")
                .set("padding", "12px");

        loginButton.addClickListener(event ->
                UI.getCurrent().navigate("login"));

        Button logoutButton = new Button("Logout");
        logoutButton.setWidth("220px");
        logoutButton.getStyle()
                .set("background-color", "#e74c3c")
                .set("color", "white")
                .set("font-size", "18px")
                .set("font-weight", "bold")
                .set("border-radius", "10px")
                .set("padding", "12px");

        logoutButton.addClickListener(event -> {
            SessionManager.logout();
            UI.getCurrent().getPage().reload();
        });

        card.add(
                roleText,
                title,
                subtitle,
                image,
                feedButton
        );

        if (loggedIn) {
            card.add(logoutButton);
        } else {
            card.add(registerButton, loginButton);
        }

        add(card);
    }
}