package com.example.fitnessfactory.data.events;

import com.example.fitnessfactory.data.models.AppUser;

import java.util.List;

public class AdminsListDataListenerEvent {

    private List<AppUser> admins;

    public AdminsListDataListenerEvent(List<AppUser> admins) {
        this.admins = admins;
    }

    public List<AppUser> getAdmins() {
        return admins;
    }
}
