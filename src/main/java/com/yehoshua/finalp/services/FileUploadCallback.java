package com.yehoshua.finalp.services;

public interface FileUploadCallback {

    void onSuccess(String filePath);

    void onError(String errorMessage);
}
