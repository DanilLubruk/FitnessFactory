package com.example.fitnessfactory.data.models;

public class Gym {

    public static final String ID_FIELD = "id";
    public static final String NAME_FILED = "name";
    public static final String ADDRESS_FIELD = "address";

    private String id;
    private String name;
    private String address;

    public Gym() {
        id = "";
        name = "";
        address = "";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static boolean isNotNull(Gym gym) {
        return gym != null
                && gym.getId() != null
                && gym.getName() != null
                && gym.getAddress() != null;
    }

    public boolean equals(Gym gym) {
        return this.getId().equals(gym.getId()) &&
                this.getName().equals(gym.getName()) &&
                this.getAddress().equals(gym.getAddress());
    }

    public void copy(Gym gym) {
        this.setId(gym.getId());
        this.setName(gym.getName());
        this.setAddress(gym.getAddress());
    }

    public static Gym.Builder builder() {
        return new Gym().new Builder();
    }

    public class Builder {

        public Builder setId(String gymId) {
            Gym.this.setId(gymId);
            return this;
        }

        public Builder setName(String name) {
            Gym.this.setName(name);
            return this;
        }

        public Builder setAddress(String address) {
            Gym.this.setAddress(address);
            return this;
        }

        public Gym build() {
            return Gym.this;
        }
    }
}
