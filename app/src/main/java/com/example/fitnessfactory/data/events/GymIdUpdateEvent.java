package com.example.fitnessfactory.data.events;

public class GymIdUpdateEvent extends BaseEvent {

    private final String gymId;

    public GymIdUpdateEvent(String gymId) {
        this.gymId = gymId;
    }

    public String getGymId() {
        return gymId;
    }
}
