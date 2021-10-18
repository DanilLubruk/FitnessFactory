package com.example.fitnessfactory.data.models;

public class Client {

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String EMAIL_FIELD = "email";

    private String id;
    private String name;
    private String email;

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

    public void copy(Client client) {
        this.setId(client.getId());
        this.setName(client.getName());
        this.setEmail(client.getEmail());
    }

    public boolean equals(Client client) {
        return this.getId().equals(client.getId())
                && this.getName().equals(client.getName())
                && this.getEmail().equals(client.getEmail());
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

        public Client build() {
            return Client.this;
        }
    }
}
