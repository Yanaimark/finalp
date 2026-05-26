package com.yehoshua.finalp.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.yehoshua.finalp.datamodels.Post;
import com.yehoshua.finalp.datamodels.User;
import com.yehoshua.finalp.services.FeedService;
import com.yehoshua.finalp.services.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("feed")
public class FeedView extends VerticalLayout {

    @Autowired
    public FeedView(FeedService feedService, UserService userService) {

        boolean loggedIn = SessionManager.isLoggedIn();

        String userId = loggedIn
                ? SessionManager.getCurrentUser().getId()
                : null;

        H1 title = new H1("Miniatures & Dioramas Feed");
        title.getStyle()
                .set("color", "#2c3e50")
                .set("margin-bottom", "20px");

        add(title);

        HorizontalLayout navBar = new HorizontalLayout();
        navBar.setWidth("100%");
        navBar.setJustifyContentMode(JustifyContentMode.CENTER);
        navBar.setSpacing(true);

        Button homeButton = new Button("🏠 Home");
        homeButton.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate(HomeView.class)));

        Button feedButton = new Button("🖼 Feed");
        feedButton.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate("feed")));

        navBar.add(homeButton, feedButton);

        if (loggedIn) {
            Button createPostButton = new Button("➕ Create Post");
            createPostButton.addClickListener(event ->
                    getUI().ifPresent(ui -> ui.navigate("create-post")));

            Button profileButton = new Button("👤 Profile");
            profileButton.addClickListener(event ->
                    getUI().ifPresent(ui -> ui.navigate("profile")));

            Button logoutButton = new Button("🚪 Logout");
            logoutButton.addClickListener(event -> {
                SessionManager.logout();
                Notification.show("Logged out. You are now a guest.");
                getUI().ifPresent(ui -> ui.navigate(HomeView.class));
            });

            navBar.add(createPostButton, profileButton, logoutButton);
        } else {
            Button loginButton = new Button("Login");
            loginButton.addClickListener(event ->
                    getUI().ifPresent(ui -> ui.navigate("login")));

            Button registerButton = new Button("Register");
            registerButton.addClickListener(event ->
                    getUI().ifPresent(ui -> ui.navigate("register")));

            navBar.add(loginButton, registerButton);
        }

        add(navBar);

        Paragraph roleText = new Paragraph("Current role: " + SessionManager.getRole());
        roleText.getStyle()
                .set("font-weight", "bold")
                .set("color", loggedIn ? "#27ae60" : "#e67e22");
        add(roleText);

        List<Post> posts = loggedIn
                ? feedService.getPersonalizedFeed(userId)
                : feedService.getAllPosts();

        if (posts.isEmpty()) {
            add(new Paragraph("No posts found."));
            return;
        }

        for (Post post : posts) {

            VerticalLayout card = new VerticalLayout();
            card.setWidth("700px");
            card.setAlignItems(Alignment.CENTER);
            card.getStyle()
                    .set("border", "1px solid #ddd")
                    .set("border-radius", "15px")
                    .set("padding", "20px")
                    .set("margin-bottom", "20px")
                    .set("background-color", "#ffffff")
                    .set("box-shadow", "0 4px 10px rgba(0,0,0,0.1)");

            H3 caption = new H3(post.getCaption());

            Paragraph tags = new Paragraph("Tags: " + post.getTags());
            Paragraph likes = new Paragraph("Likes: " + post.getLikesCount());

            card.add(caption);

            if (post.getMediaUrl() != null && !post.getMediaUrl().isEmpty()) {
                Image image = new Image(post.getMediaUrl(), "Post image");
                image.setWidth("100%");
                image.getStyle()
                        .set("border-radius", "10px");
                card.add(image);
            }

            if (loggedIn) {
                boolean alreadyLiked =
                        SessionManager.getCurrentUser().getLikedPostIds() != null &&
                        SessionManager.getCurrentUser()
                                .getLikedPostIds()
                                .contains(post.getId());

                Button likeButton = new Button(
                        alreadyLiked ? "👎 Remove Like" : "👍 Like"
                );

                likeButton.getStyle()
                        .set("background-color", alreadyLiked ? "#e74c3c" : "#3498db")
                        .set("color", "white")
                        .set("border-radius", "8px");

                likeButton.addClickListener(event -> {
                    String currentUserId = SessionManager.getCurrentUser().getId();

                    if (alreadyLiked) {
                        userService.unlikePost(currentUserId, post.getId());
                        Notification.show("Like removed.");
                    } else {
                        userService.likePost(currentUserId, post.getId());
                        Notification.show("Post liked!");
                    }

                    User updatedUser = userService.login(
                            SessionManager.getCurrentUser().getUsername(),
                            SessionManager.getCurrentUser().getPassword()
                    );

                    SessionManager.setCurrentUser(updatedUser);

                    getUI().ifPresent(ui -> ui.getPage().reload());
                });

                card.add(tags, likes, likeButton);
            } else {
                Button loginToLikeButton = new Button("Login to Like");
                loginToLikeButton.getStyle()
                        .set("background-color", "#95a5a6")
                        .set("color", "white")
                        .set("border-radius", "8px");

                loginToLikeButton.addClickListener(event ->
                        getUI().ifPresent(ui -> ui.navigate("login"))
                );

                card.add(tags, likes, loginToLikeButton);
            }

            add(card);
        }
    }
}