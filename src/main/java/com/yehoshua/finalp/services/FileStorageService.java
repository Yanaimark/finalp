package com.yehoshua.finalp.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

@Service
public class FileStorageService {

    public void saveFileAsync(
            InputStream inputStream,
            String originalFileName,
            FileUploadCallback callback) {

        new Thread(() -> {
            try {
                // Simulates a long task, such as image processing or large file saving
                Thread.sleep(1500);

                File uploadDir = new File("uploads");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String fileName =
                        System.currentTimeMillis() + "_" + originalFileName;

                File file = new File(uploadDir, fileName);

                try (FileOutputStream fos = new FileOutputStream(file)) {
                    inputStream.transferTo(fos);
                }

                String savedPath = "/uploads/" + fileName;

                // Callback when the long task finishes successfully
                callback.onSuccess(savedPath);

            } catch (IOException e) {
                callback.onError("File upload failed.");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                callback.onError("File upload was interrupted.");
            }
        }).start();
    }
}