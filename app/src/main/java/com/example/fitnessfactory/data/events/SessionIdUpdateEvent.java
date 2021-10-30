package com.example.fitnessfactory.data.events;

public class SessionIdUpdateEvent extends BaseEvent {

    private final String sessionId;

    public SessionIdUpdateEvent(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }
}
