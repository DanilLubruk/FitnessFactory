package com.example.fitnessfactory.data.events;

public class PersonnelEmailUpdateEvent extends StickyEvent {

    private final String personnelEmail;

    public PersonnelEmailUpdateEvent(String personnelEmail) {
        this.personnelEmail = personnelEmail;
    }

    public String getPersonnelEmail() {
        return personnelEmail;
    }
}
