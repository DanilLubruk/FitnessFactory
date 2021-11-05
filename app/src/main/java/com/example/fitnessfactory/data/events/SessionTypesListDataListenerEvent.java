package com.example.fitnessfactory.data.events;

import com.example.fitnessfactory.data.models.SessionType;

import java.util.List;

public class SessionTypesListDataListenerEvent {

    private List<SessionType> sessionTypes;

    public SessionTypesListDataListenerEvent(List<SessionType> sessionTypes) {
        this.sessionTypes = sessionTypes;
    }

    public List<SessionType> getSessionTypes() {
        return sessionTypes;
    }
}
