package com.example.fitnessfactory.data.events;

import com.example.fitnessfactory.data.models.Session;

import java.util.List;

public class SessionsCalendarDataListenerEvent {

    private List<Session> sessions;

    public SessionsCalendarDataListenerEvent(List<Session> sessions) {
        this.sessions = sessions;
    }

    public List<Session> getSessions() {
        return sessions;
    }
}
