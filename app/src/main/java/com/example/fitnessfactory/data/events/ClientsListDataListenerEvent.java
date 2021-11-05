package com.example.fitnessfactory.data.events;

import com.example.fitnessfactory.data.models.Client;

import java.util.List;

public class ClientsListDataListenerEvent {

    private List<Client> clientsList;

    public ClientsListDataListenerEvent(List<Client> clientsList) {
        this.clientsList = clientsList;
    }

    public List<Client> getClientsList() {
        return clientsList;
    }
}
