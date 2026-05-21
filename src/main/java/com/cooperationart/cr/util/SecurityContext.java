package com.cooperationart.cr.util;

public class SecurityContext {
    private static String currentUser = "SYSTEM";

    public static void setCurrentUser(String user) {
        currentUser = user;
    }

    public static String getCurrentUser() {
        return currentUser;
    }
}
