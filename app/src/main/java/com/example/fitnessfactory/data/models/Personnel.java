package com.example.fitnessfactory.data.models;

import java.util.List;

public class Personnel {

    public static final String ID_FIELD = "id";
    public static final String USER_EMAIL_FIELD = "userEmail";
    public static final String GYMS_ARRAY_FIELD = "gymsIds";

    private String id;
    private String userEmail;
    private List<String> gymsIds;

    public Personnel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public static Personnel.Builder builder() {
        return new Personnel().new Builder();
    }

    public class Builder {

        public Personnel.Builder setId(String id) {
            Personnel.this.setId(id);
            return this;
        }

        public Personnel.Builder setUserEmail(String userEmail) {
            Personnel.this.setUserEmail(userEmail);
            return this;
        }

        public Personnel.Builder setGymsIds(List<String> gymsIds) {
            Personnel.this.setGymsIds(gymsIds);
            return this;
        }

        public Personnel build() {
            return Personnel.this;
        }
    }
}
