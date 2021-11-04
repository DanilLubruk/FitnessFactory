package com.example.fitnessfactory.data.events;

import java.util.List;

public class SessionsCoachesListDataListenerEvent extends BaseEvent {

    private final List<String> coachesIds;

    public SessionsCoachesListDataListenerEvent(List<String> coachesIds) {
        this.coachesIds = coachesIds;
    }

    public List<String> getCoachesIds() {
        return coachesIds;
    }
}
