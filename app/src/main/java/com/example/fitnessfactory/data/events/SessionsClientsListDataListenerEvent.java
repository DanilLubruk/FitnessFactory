package com.example.fitnessfactory.data.events;

import java.util.List;

public class SessionsClientsListDataListenerEvent {

    private List<String> clientsIds;

    public SessionsClientsListDataListenerEvent(List<String> clientsIds) {
        this.clientsIds = clientsIds;
    }

    public List<String> getClientsIds() {
        return clientsIds;
    }
}
