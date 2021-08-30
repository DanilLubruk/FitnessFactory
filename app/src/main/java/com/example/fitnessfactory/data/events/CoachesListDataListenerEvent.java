package com.example.fitnessfactory.data.events;

import com.example.fitnessfactory.data.dataListeners.CoachesListDataListener;
import com.example.fitnessfactory.data.models.AppUser;

import java.util.List;

public class CoachesListDataListenerEvent extends BaseEvent {

    private List<AppUser> coaches;

    public CoachesListDataListenerEvent(List<AppUser> coaches) {
        this.coaches = coaches;
    }

    public List<AppUser> getCoaches() {
        return coaches;
    }
}
