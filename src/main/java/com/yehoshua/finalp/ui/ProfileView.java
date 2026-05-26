package com.yehoshua.finalp.ui;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.yehoshua.finalp.datamodels.Post;
import com.yehoshua.finalp.datamodels.User;
import com.yehoshua.finalp.repositories.PostRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("profile")
public class ProfileView extends VerticalLayout {

    @Autowired
    public ProfileView(PostRepository postRepository) {

        if (!SessionManager.isLoggedIn()) {
            Notification.show("You are currently a guest. Please login first.");
            getUI().ifPresent(ui -> ui.navigate("login"));
            return;
        }

        // Make sure the user is logged in
        if (SessionManager.getCurrentUser() == null) {
            getUI().ifPresent(ui -> ui.navigate("login"));
            return;
        }

        // Get current user
        User user = SessionManager.getCurrentUser();

        // Page layout
        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);
        add(new NavBar());

        // Title
        H1 title = new H1("User Profile");
        title.getStyle().set("color", "#2c3e50");

        // Basic information
        Paragraph username = new Paragraph("Username: " + user.getUsername());
        Paragraph email = new Paragraph("Email: " + user.getEmail());
        Paragraph createdAt = new Paragraph("Member Since: " + user.getCreatedAt());

        // Liked posts count
        int likedPostsCount = user.getLikedPostIds() == null
                ? 0
                : user.getLikedPostIds().size();

        Paragraph likedPosts = new Paragraph(
                "Liked Posts: " + likedPostsCount
        );

        // Interests
        String interestsText =
                (user.getInterests() == null || user.getInterests().isEmpty())
                        ? "No interests recorded yet."
                        : user.getInterests().toString();

        Paragraph interests = new Paragraph(
                "Interests: " + interestsText
        );

        // Add basic info
        add(
                title,
                username,
                email,
                createdAt,
                likedPosts,
                interests
        );

        // Preference Vector
        H3 preferencesTitle = new H3("Preference Vector");
        add(preferencesTitle);

        if (user.getPreferenceVector() == null
                || user.getPreferenceVector().isEmpty()) {

            add(new Paragraph(
                    "No preferences yet. Like some posts to build your profile."
            ));
        } else {
            for (Map.Entry<String, Integer> entry
                    : user.getPreferenceVector().entrySet()) {

                add(new Paragraph(
                        entry.getKey() + ": " + entry.getValue()
                ));
            }
        }

        // ===== USER'S OWN POSTS =====
        H3 myPostsTitle = new H3("My Posts");
        add(myPostsTitle);

        List<Post> myPosts = postRepository.findAll()
                .stream()
                .filter(post -> user.getId().equals(post.getUserId()))
                .toList();

        if (myPosts.isEmpty()) {
            add(new Paragraph("You haven't created any posts yet."));
        } else {
            for (Post post : myPosts) {

                VerticalLayout card = new VerticalLayout();
                card.setWidth("700px");
                card.getStyle()
                        .set("border", "1px solid #ddd")
                        .set("border-radius", "15px")
                        .set("padding", "20px")
                        .set("margin-bottom", "20px")
                        .set("background-color", "#ffffff")
                        .set("box-shadow", "0 4px 10px rgba(0,0,0,0.1)");

                // Caption
                H3 caption = new H3(post.getCaption());

                // Image
                if (post.getMediaUrl() != null
                        && !post.getMediaUrl().isEmpty()) {

                    Image image = new Image(
                            post.getMediaUrl(),
                            "Post image"
                    );
                    image.setWidth("100%");
                    image.getStyle()
                            .set("border-radius", "10px");

                    card.add(image);
                }

                // Tags and likes
                Paragraph tags = new Paragraph(
                        "Tags: " + post.getTags()
                );

                Paragraph likes = new Paragraph(
                        "Likes: " + post.getLikesCount()
                );

                card.add(caption, tags, likes);

                add(card);
            }
        }

        // Navigation buttons
        Button feedButton = new Button(
                "🏠 Back to Feed",
                event -> getUI().ifPresent(ui -> ui.navigate("feed"))
        );

        Button createPostButton = new Button(
                "➕ Create Post",
                event -> getUI().ifPresent(ui -> ui.navigate("create-post"))
        );

        HorizontalLayout buttons = new HorizontalLayout(
                feedButton,
                createPostButton
        );

        add(buttons);
    }
}