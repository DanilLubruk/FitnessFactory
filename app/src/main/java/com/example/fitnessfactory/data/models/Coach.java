package com.example.fitnessfactory.data.models;

import java.util.List;

public class Coach implements Personnel {

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

    public static Coach.Builder builder() {
        return new Coach().new Builder();
    }

    public class Builder {

        public Builder setUserEmail(String userEmail) {
            Coach.this.setUserEmail(userEmail);
            return this;
        }

        public Builder setGymsIds(List<String> gymsIds) {
            Coach.this.setGymsIds(gymsIds);
            return this;
        }

        public Coach build() {
            return Coach.this;
        }
    }
}
