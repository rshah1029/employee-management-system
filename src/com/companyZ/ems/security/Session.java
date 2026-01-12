package com.companyZ.ems.security;

import com.companyZ.ems.model.User;

public class Session {
    private static User currentUser = null;
    
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public static boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }
    
    public static void logout() {
        currentUser = null;
    }
}