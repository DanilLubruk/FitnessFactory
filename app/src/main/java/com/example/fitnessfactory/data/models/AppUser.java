package com.example.fitnessfactory.data.models;

public class AppUser {

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String EMAIL_FILED = "email";

    private String id;
    private String name;
    private String email;

    public AppUser() {

    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static AppUser.Builder builder() {
        return new AppUser().new Builder();
    }

    public class Builder {

        public Builder setId(String id) {
            AppUser.this.setId(id);
            return this;
        }

        public Builder setName(String name) {
            AppUser.this.setName(name);
            return this;
        }

        public Builder setEmail(String email) {
            AppUser.this.setEmail(email);
            return this;
        }

        public AppUser build() {
            return AppUser.this;
        }
    }
}
