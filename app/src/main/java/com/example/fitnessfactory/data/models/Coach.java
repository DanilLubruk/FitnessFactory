package com.example.fitnessfactory.data.models;

import java.util.List;

public class Coach {

    public static final String USER_EMAIL_FIELD = "userEmail";
    public static final String GYMS_ARRAY_FIELD = "gymsIds";

    private String userEmail;
    private List<String> gymsIds;

    public Coach() {

    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<String> getGymsIds() {
        return gymsIds;
    }

    public void setGymsIds(List<String> gymsIds) {
        this.gymsIds = gymsIds;
    }
}
