package com.example.fitnessfactory.data.events;

import com.example.fitnessfactory.data.models.Personnel;

import java.util.List;

public class ClientsListDataListenerEvent {

    private List<Personnel> clientsList;

    public ClientsListDataListenerEvent(List<Personnel> clientsList) {
        this.clientsList = clientsList;
    }

    public List<Personnel> getClientsList() {
        return clientsList;
    }
}
