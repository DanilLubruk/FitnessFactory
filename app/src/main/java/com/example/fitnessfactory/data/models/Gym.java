package com.example.fitnessfactory.data.models;

public class Gym {

    public static final String ID_FIELD = "id";
    public static final String NAME_FILED = "name";
    public static final String ADDRESS_FIELD = "address";

    private String id;
    private String name;
    private String address;

    public Gym() {

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

    public boolean equals(Gym gym) {
        return this.getName().equals(gym.getName()) &&
                this.getAddress().equals(gym.getAddress()) &&
                this.getId().equals(gym.getId());
    }

    public void copy(Gym gym) {
        this.setId(gym.getId());
        this.setName(gym.getName());
        this.setAddress(gym.getAddress());
    }
}
