package com.yehoshua.finalp.ui;

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
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);

        getStyle()
                .set("background", "linear-gradient(135deg, #1e3c72 0%, #2a5298 100%)");

        // Navbar at the top
        add(new NavBar());

        // Center area for the card
        VerticalLayout centerLayout = new VerticalLayout();
        centerLayout.setSizeFull();
        centerLayout.setAlignItems(Alignment.CENTER);
        centerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centerLayout.setPadding(true);

        // Main card
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
                "Share your miniatures, dioramas, and painting projects.\n" +
                "By Yehoshua Naimark\n" +
                "2026"
        );

        subtitle.getStyle()
                .set("color", "#666")
                .set("font-size", "18px")
                .set("text-align", "center")
                .set("white-space", "pre-line")
                .set("margin-bottom", "20px");

        Image image = new Image("Picture2.png", "Miniatures and dioramas");
        image.setWidth("100%");
        image.setMaxWidth("500px");
        image.getStyle()
                .set("border-radius", "15px")
                .set("box-shadow", "0 4px 15px rgba(0,0,0,0.15)")
                .set("margin-bottom", "20px");

        card.add(
                roleText,
                title,
                subtitle,
                image
        );

        centerLayout.add(card);
        add(centerLayout);
    }
}