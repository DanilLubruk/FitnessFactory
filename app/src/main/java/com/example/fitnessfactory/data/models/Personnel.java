package com.example.fitnessfactory.data.models;

import java.util.List;

public interface Personnel {

    String getUserEmail();

    List<String> getGymsIds();

    void setUserEmail(String userEmail);

    void setGymsIds(List<String> gymsIds);
}
