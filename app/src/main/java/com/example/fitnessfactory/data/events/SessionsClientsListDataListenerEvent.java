package com.example.fitnessfactory.data.events;

import com.example.fitnessfactory.data.models.Client;

import java.util.List;

public class SessionsClientsListDataListenerEvent extends BaseEvent {

    private List<Client> clients;

    public SessionsClientsListDataListenerEvent(List<Client> clients) {
        this.clients = clients;
    }

    public List<Client> getClients() {
        return clients;
    }
}
