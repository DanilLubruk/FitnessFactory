package com.example.fitnessfactory.data.models;

public class Gym {

    public static final String ADDRESS_FIELD = "address";

    private String id;
    private String address;

    public Gym() {

    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
