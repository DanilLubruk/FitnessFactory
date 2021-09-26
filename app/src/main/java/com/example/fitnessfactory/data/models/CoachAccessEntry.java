package com.example.fitnessfactory.data.models;

public class CoachAccessEntry {

    public static final String USER_EMAIL_FIELD = "userEmail";
    public static final String OWNER_ID_FIELD = "ownerId";

    private String userEmail;

    private String ownerId;

    public CoachAccessEntry() {

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

    public static CoachAccessEntry.Builder builder() {
        return new CoachAccessEntry().new Builder();
    }

    public class Builder {

        public Builder setUserEmail(String userEmail) {
            CoachAccessEntry.this.setUserEmail(userEmail);
            return this;
        }

        public Builder setOwnerId(String ownerId) {
            CoachAccessEntry.this.setOwnerId(ownerId);
            return this;
        }

        public CoachAccessEntry build() {
            return CoachAccessEntry.this;
        }
    }
}
