package com.example.fitnessfactory.data.events;

import java.util.List;

public class SessionsCoachesListDataListenerEvent extends BaseEvent {

    private final List<String> coachesEmails;

    public SessionsCoachesListDataListenerEvent(List<String> coachesEmails) {
        this.coachesEmails = coachesEmails;
    }

    public List<String> getCoachesEmails() {
        return coachesEmails;
    }
}
