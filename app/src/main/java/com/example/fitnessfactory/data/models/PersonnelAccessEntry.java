package com.example.fitnessfactory.data.models;

public class PersonnelAccessEntry {

    public static final String USER_ID_FIELD = "userId";
    public static final String OWNER_ID_FIELD = "ownerId";

    private String userId;
    private String ownerId;

    public PersonnelAccessEntry() {

    }

    public String getUserId() {
        return userId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public static PersonnelAccessEntry.Builder builder() {
        return new PersonnelAccessEntry().new Builder();
    }

    public class Builder {

        public PersonnelAccessEntry.Builder setUserId(String userEmail) {
            PersonnelAccessEntry.this.userId = userEmail;
            return this;
        }

        public PersonnelAccessEntry.Builder setOwnerId(String ownerId) {
            PersonnelAccessEntry.this.ownerId = ownerId;
            return this;
        }

        public PersonnelAccessEntry build() {
            return PersonnelAccessEntry.this;
        }
    }
}
