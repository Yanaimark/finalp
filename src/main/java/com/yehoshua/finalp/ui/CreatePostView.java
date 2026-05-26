package com.yehoshua.finalp.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.yehoshua.finalp.datamodels.Post;
import com.yehoshua.finalp.datamodels.User;
import com.yehoshua.finalp.services.PostService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;

@Route("create-post")
public class CreatePostView extends VerticalLayout {

    @Autowired
    public CreatePostView(PostService postService) {

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

        User user = SessionManager.getCurrentUser();

        // Page layout
        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);
        add(new NavBar());

        // Title
        H1 title = new H1("Create New Post");

        // Caption
        TextArea captionField = new TextArea("Caption");
        captionField.setWidth("700px");
        captionField.setPlaceholder(
                "Describe your miniature, diorama, or painting project..."
        );

        // Media type
        TextField mediaTypeField = new TextField("Media Type");
        mediaTypeField.setValue("image");
        mediaTypeField.setWidth("700px");

        // Tags
        TextField tagsField = new TextField("Tags (comma separated)");
        tagsField.setWidth("700px");
        tagsField.setPlaceholder(
                "warhammer40k, painting, blue, gold, weathering"
        );

        // File upload
        
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);

        // Accept common image formats
        upload.setAcceptedFileTypes(
        ".jpg",
                             ".jpeg",
                             ".png",
                             ".gif",
                             "image/jpeg",
                             "image/jpg",
                             "image/png",
                             "image/gif"
        );

        upload.setMaxFiles(1);
        upload.setWidth("700px");

         upload.addSucceededListener(event -> {
            Notification.show("Uploaded: " + event.getFileName());
        });

        upload.addFileRejectedListener(event -> {
            Notification.show("Upload rejected: " + event.getErrorMessage());
        });

        // Store uploaded file path
        final String[] savedFilePath = new String[1];

        upload.addSucceededListener(event -> {
            try {
                File uploadDir = new File("uploads");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String fileName = System.currentTimeMillis() + "_" + event.getFileName();

                File file = new File(uploadDir, fileName);

                try (FileOutputStream fos = new FileOutputStream(file)) {
                    buffer.getInputStream().transferTo(fos);
                }

                // This is the URL that will be stored in MongoDB
                savedFilePath[0] = "/uploads/" + fileName;

            } catch (IOException e) {
                Notification.show("File upload failed.");
                e.printStackTrace();
            }
        });

        // Buttons
        Button postButton = new Button("🚀 Post");
        Button cancelButton = new Button("← Cancel");

        postButton.getStyle()
                .set("background-color", "#27ae60")
                .set("color", "white")
                .set("font-weight", "bold")
                .set("border-radius", "8px");

        cancelButton.getStyle()
                .set("background-color", "#95a5a6")
                .set("color", "white")
                .set("font-weight", "bold")
                .set("border-radius", "8px");

        // Cancel action
        cancelButton.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate("feed")));

        // Post action
        postButton.addClickListener(event -> {

            // Validate caption
            if (captionField.getValue().trim().isEmpty()) {
                Notification.show("Please enter a caption.");
                return;
            }

            // Validate file upload
            if (savedFilePath[0] == null) {
                Notification.show("Please upload an image.");
                return;
            }

            // Convert tags string to list
            List<String> tags = Arrays.stream(
                    tagsField.getValue().split(","))
                    .map(String::trim)
                    .filter(tag -> !tag.isEmpty())
                    .toList();

            // Create post
            Post post = new Post();
            post.setUserId(user.getId());
            post.setCaption(captionField.getValue());
            post.setMediaUrl(savedFilePath[0]);   // local file path
            post.setMediaType(mediaTypeField.getValue());
            post.setTags(tags);

            // Save to MongoDB
            postService.createPost(post);

            Notification.show("Post created successfully!");

            // Return to feed
            getUI().ifPresent(ui -> ui.navigate("feed"));
        });

        // Button layout
        HorizontalLayout buttons =
                new HorizontalLayout(postButton, cancelButton);

        // Add components
        add(
                title,
                captionField,
                mediaTypeField,
                tagsField,
                upload,
                buttons
        );
    }
}