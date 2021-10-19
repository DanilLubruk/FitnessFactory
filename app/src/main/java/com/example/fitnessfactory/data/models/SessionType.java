package com.example.fitnessfactory.data.models;

public class SessionType {

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String PEOPLE_AMOUNT_FIELD = "peopleAmount";
    public static final String PRICE_FIELD = "price";

    private String id;
    private String name;
    private int peopleAmount;
    private float price;

    public SessionType() {

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

    public int getPeopleAmount() {
        return peopleAmount;
    }

    public String getPeopleAmountString() {
        return String.valueOf(getPeopleAmount());
    }

    public void setPeopleAmount(int peopleAmount) {
        this.peopleAmount = peopleAmount;
    }

    public float getPrice() {
        return price;
    }

    public String getPriceString() {
        return String.valueOf(getPrice());
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
