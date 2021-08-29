package com.example.fitnessfactory.data.events;

import com.example.fitnessfactory.data.models.Gym;

import java.util.List;

public class GymsListDataListenerEvent extends BaseEvent {

    private final List<Gym> gyms;

    public GymsListDataListenerEvent(List<Gym> gyms) {
        this.gyms = gyms;
    }

    public List<Gym> getGyms() {
        return gyms;
    }
}
