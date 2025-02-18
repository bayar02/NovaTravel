package tn.esprit.utils;

import tn.esprit.entities.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    
    private SessionManager() {}
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public boolean isAdmin() {
        return isLoggedIn() && currentUser.getRole() == User.Role.ADMIN;
    }
    
    public boolean isAgent() {
        return isLoggedIn() && currentUser.getRole() == User.Role.AGENT;
    }
    
    public boolean isRegularUser() {
        return isLoggedIn() && currentUser.getRole() == User.Role.REGULAR_USER;
    }
    
    public void clearSession() {
        currentUser = null;
    }
} 