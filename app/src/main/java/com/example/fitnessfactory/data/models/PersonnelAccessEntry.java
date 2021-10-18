package com.example.fitnessfactory.data.models;

public class PersonnelAccessEntry {

    public static final String USER_EMAIL_FIELD = "userEmail";
    public static final String OWNER_ID_FIELD = "ownerId";

    private String userEmail;

    private String ownerId;

    public PersonnelAccessEntry() {

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

    public static PersonnelAccessEntry.Builder builder() {
        return new PersonnelAccessEntry().new Builder();
    }

    public class Builder {

        public PersonnelAccessEntry.Builder setUserEmail(String userEmail) {
            PersonnelAccessEntry.this.userEmail = userEmail;
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
