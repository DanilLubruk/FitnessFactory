package com.example.fitnessfactory.data.models;

import java.util.List;

public class Client {

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String EMAIL_FIELD = "email";
    public static final String SESSIONS_IDS_FIELD = "sessions";

    private String id;
    private String name;
    private String email;
    private List<String> sessionsIds;

    public Client() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getSessionsIds() {
        return sessionsIds;
    }

    public void setSessionsIds(List<String> sessionsIds) {
        this.sessionsIds = sessionsIds;
    }

    public static boolean isNotNull(Client client) {
        return client != null
                && client.getId() != null
                && client.getName() != null
                && client.getEmail() != null
                && client.getSessionsIds() != null;
    }

    public void copy(Client client) {
        this.setId(client.getId());
        this.setName(client.getName());
        this.setEmail(client.getEmail());
        this.setSessionsIds(client.getSessionsIds());
    }

    public boolean equals(Client client) {
        return this.getId().equals(client.getId())
                && this.getName().equals(client.getName())
                && this.getEmail().equals(client.getEmail())
                && this.getSessionsIds().equals(client.getSessionsIds());
    }

    public Client.Builder builder() {
        return new Client().new Builder();
    }

    public class Builder {

        public Builder setId(String id) {
            Client.this.setId(id);
            return this;
        }

        public Builder setName(String name) {
            Client.this.setName(name);
            return this;
        }

        public Builder setEmail(String email) {
            Client.this.setEmail(email);
            return this;
        }

        public Builder setSessionsIds(List<String> sessionsIds) {
            Client.this.setSessionsIds(sessionsIds);
            return this;
        }

        public Client build() {
            return Client.this;
        }
    }
}
