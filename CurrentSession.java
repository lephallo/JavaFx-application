package com.example.vehivlesproject; // Corrected package name

/**
 * Singleton class to manage the current user's session.
 */
public class CurrentSession {
    private static CurrentSession instance;
    private int userId;

    // Private constructor to prevent instantiation from other classes.
    private CurrentSession() {}

    /**
     * Returns the single instance of CurrentSession, creating it if necessary.
     */
    public static CurrentSession getInstance() {
        if (instance == null) {
            instance = new CurrentSession();
        }
        return instance;
    }

    /**
     * Sets the user ID for the current session.
     * @param userId the user ID to be set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Returns the user ID for the current session.
     * @return the current user ID.
     */
    public int getUserId() {
        return userId;
    }
}