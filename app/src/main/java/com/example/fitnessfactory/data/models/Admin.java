package com.example.fitnessfactory.data.models;

import android.os.Build;

import java.util.List;

public class Admin {

    public static final String USER_EMAIL_FIELD = "userEmail";
    public static final String GYMS_ARRAY_FIELD = "gymsIds";

    private String userEmail;
    private List<String> gymsIds;

    public Admin() {

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

    public static Admin.Builder builder() {
        return new Admin().new Builder();
    }

    public class Builder {

        public Builder setUserEmail(String userEmail) {
            Admin.this.setUserEmail(userEmail);
            return this;
        }

        public Builder setGymsIds(List<String> gymsIds) {
            Admin.this.setGymsIds(gymsIds);
            return this;
        }

        public Admin build() {
            return Admin.this;
        }
    }
}
