package com.example.fitnessfactory.data.models;

import java.util.List;

public class AccessEntry {

    public static String USER_EMAIL_FIELD = "userEmail";
    public static String OWNER_ID_FIELD = "ownerId";
    public static String GYMS_FIELD = "gyms";

    private String userEmail;

    private String ownerId;

    private List<String> gyms;

    public AccessEntry() {

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

    public List<String> getGyms() {
        return gyms;
    }

    public void setGyms(List<String> gyms) {
        this.gyms = gyms;
    }
}
