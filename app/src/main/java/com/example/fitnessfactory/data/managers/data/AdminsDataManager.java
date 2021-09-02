package com.example.fitnessfactory.data.managers.data;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;

import javax.inject.Inject;

public class AdminsDataManager extends PersonnelDataManager {

    @Inject
    OwnerAdminsRepository ownerAdminsRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    OwnerGymRepository ownerGymRepository;

    public AdminsDataManager() {
        FFApp.get().getAppComponent().inject(this);
    }

    @Override
    protected OwnerAdminsRepository getOwnerRepository() {
        return ownerAdminsRepository;
    }

    @Override
    protected UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    protected OwnerGymRepository getGymRepository() {
        return ownerGymRepository;
    }
}
