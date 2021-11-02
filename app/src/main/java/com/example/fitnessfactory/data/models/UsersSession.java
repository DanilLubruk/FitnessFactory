package com.example.fitnessfactory.data.models;

public class UsersSession {

    public static final String ID_FIELD = "sessionId";

    private String sessionId;

    public UsersSession() {

    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public static UsersSession.Builder builder() {
        return new UsersSession(). new Builder();
    }

    public class Builder {

        public Builder setSessionId(String sessionId) {
            UsersSession.this.setSessionId(sessionId);
            return this;
        }

        public UsersSession build() {
            return UsersSession.this;
        }
    }
}
