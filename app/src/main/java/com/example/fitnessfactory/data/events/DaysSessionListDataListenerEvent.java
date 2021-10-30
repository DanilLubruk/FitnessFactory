package com.example.fitnessfactory.data.events;

import com.example.fitnessfactory.data.models.Session;

import java.util.List;

public class DaysSessionListDataListenerEvent extends BaseEvent {

    private List<Session> sessions;

    public DaysSessionListDataListenerEvent(List<Session> sessions) {
        this.sessions = sessions;
    }

    public List<Session> getSessions() {
        return sessions;
    }
}
