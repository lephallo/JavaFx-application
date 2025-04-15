package com.example.vehivlesproject;

public class User {
    private String username;
    private String password; // Ideally, this should be a hashed password for security
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
