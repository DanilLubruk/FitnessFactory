package com.example.fitnessfactory.data.models;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.utils.ResUtils;

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

    public static boolean isNotNull(SessionType sessionType) {
        return sessionType != null
                && sessionType.getId() != null
                && sessionType.getName() != null;
    }

    public void copy(SessionType sessionType) {
        this.setId(sessionType.getId());
        this.setName(sessionType.getName());
        this.setPeopleAmount(sessionType.getPeopleAmount());
        this.setPrice(sessionType.getPrice());
    }

    public boolean equals(SessionType sessionType) {
        return this.getId().equals(sessionType.getId())
                && this.getName().equals(sessionType.getName())
                && this.getPeopleAmount() == sessionType.getPeopleAmount()
                && this.getPrice() == sessionType.getPrice();
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

    public void setPeopleAmount(int peopleAmount) {
        this.peopleAmount = peopleAmount;
    }

    public String getPeopleAmountValue() {
        return String.valueOf(getPeopleAmount());
    }

    public void setPeopleAmountValue(String value) {
        try {
            setPeopleAmount(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public String getPeopleAmountString() {
        return String.valueOf(getPeopleAmount())
                .concat(" ")
                .concat(ResUtils.getString(R.string.caption_people));
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPriceString() {
        return String.valueOf(getPrice()).concat(" ").concat(AppPrefs.currencySign().getValue());
    }

    public String getPriceValue() {
        return String.valueOf(getPrice());
    }

    public void setPriceValue(String value) {
        try {
            setPrice(Float.parseFloat(value));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
