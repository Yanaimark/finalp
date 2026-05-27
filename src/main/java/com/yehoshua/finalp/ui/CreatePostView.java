package com.yehoshua.finalp.ui;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.yehoshua.finalp.datamodels.Post;
import com.yehoshua.finalp.datamodels.User;
import com.yehoshua.finalp.services.FileStorageService;
import com.yehoshua.finalp.services.FileUploadCallback;
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
    public CreatePostView(PostService postService, FileStorageService fileStorageService) {

        if (!SessionManager.isLoggedIn()) {
            Notification.show("You are currently a guest. Please login first.");
            getUI().ifPresent(ui -> ui.navigate("login"));
            return;
        }

        User user = SessionManager.getCurrentUser();

        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);

        add(new NavBar());

        H1 title = new H1("Create New Post");

        TextArea captionField = new TextArea("Caption");
        captionField.setWidth("700px");
        captionField.setPlaceholder(
                "Describe your miniature, diorama, or painting project..."
        );

        TextField mediaTypeField = new TextField("Media Type");
        mediaTypeField.setValue("image");
        mediaTypeField.setWidth("700px");

        TextField tagsField = new TextField("Tags (comma separated)");
        tagsField.setWidth("700px");
        tagsField.setPlaceholder(
                "warhammer40k, painting, blue, gold, weathering"
        );

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);

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

        final String[] savedFilePath = new String[1];

        upload.addSucceededListener(event -> {

            Notification.show("Upload started. Please wait...");

            fileStorageService.saveFileAsync(
                    buffer.getInputStream(),
                    event.getFileName(),
                    new FileUploadCallback() {

                        @Override
                        public void onSuccess(String filePath) {
                            savedFilePath[0] = filePath;

                            getUI().ifPresent(ui -> ui.access(() ->
                                    Notification.show("Image uploaded successfully!")
                            ));
                        }

                        @Override
                        public void onError(String errorMessage) {
                            getUI().ifPresent(ui -> ui.access(() ->
                                    Notification.show(errorMessage)
                            ));
                        }
                    }
            );
        });

        upload.addFileRejectedListener(event ->
                Notification.show("Upload rejected: " + event.getErrorMessage())
        );

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

        cancelButton.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate("feed"))
        );

        postButton.addClickListener(event -> {

            if (captionField.getValue().trim().isEmpty()) {
                Notification.show("Please enter a caption.");
                return;
            }

            if (savedFilePath[0] == null) {
                Notification.show("Please upload an image and wait until it finishes.");
                return;
            }

            List<String> tags = Arrays.stream(tagsField.getValue().split(","))
                    .map(String::trim)
                    .filter(tag -> !tag.isEmpty())
                    .toList();

            Post post = new Post();
            post.setUserId(user.getId());
            post.setCaption(captionField.getValue());
            post.setMediaUrl(savedFilePath[0]);
            post.setMediaType(mediaTypeField.getValue());
            post.setTags(tags);

            postService.createPost(post);

            Notification.show("Post created successfully!");
            getUI().ifPresent(ui -> ui.navigate("feed"));
        });

        HorizontalLayout buttons = new HorizontalLayout(postButton, cancelButton);

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