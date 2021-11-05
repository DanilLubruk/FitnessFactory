package com.example.fitnessfactory.data.events;

public class SessionIdUpdateEvent extends StickyEvent {

    private final String sessionId;

    public SessionIdUpdateEvent(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }
}
