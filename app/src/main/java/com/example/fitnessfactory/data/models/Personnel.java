package com.example.fitnessfactory.data.models;

import java.util.List;

public class Personnel {

    public static final String USER_ID_FIELD = "userId";
    public static final String GYMS_ARRAY_FIELD = "gymsIds";

    private String userId;
    private List<String> gymsIds;

    public Personnel() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

        public Personnel.Builder setUserEmail(String userEmail) {
            Personnel.this.setUserId(userEmail);
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
