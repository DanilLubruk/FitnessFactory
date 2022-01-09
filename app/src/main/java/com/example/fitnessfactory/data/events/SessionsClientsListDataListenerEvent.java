package com.example.fitnessfactory.data.events;

import java.util.List;

public class SessionsClientsListDataListenerEvent {

    private List<String> clientsEmails;

    public SessionsClientsListDataListenerEvent(List<String> clientsIds) {
        this.clientsEmails = clientsIds;
    }

    public List<String> getClientsEmails() {
        return clientsEmails;
    }
}
