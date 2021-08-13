package com.example.fitnessfactory.data.models;

public class AdminAccessEntry {

    public static String USER_EMAIL_FIELD = "userEmail";
    public static String OWNER_ID_FIELD = "ownerId";

    private String userEmail;

    private String ownerId;

    public AdminAccessEntry() {

    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
