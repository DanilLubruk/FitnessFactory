package com.example.fitnessfactory.data.models;

public class Gym {

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
}
