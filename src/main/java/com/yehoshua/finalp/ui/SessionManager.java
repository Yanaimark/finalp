package com.yehoshua.finalp.ui;

import com.yehoshua.finalp.datamodels.User;

public class SessionManager {

    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static String getRole() {
        return isLoggedIn() ? "USER" : "GUEST";
    }

    public static void logout() {
        currentUser = null;
    }
}