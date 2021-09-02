package com.example.fitnessfactory.data.managers.data;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import javax.inject.Inject;

public class AdminsDataManager extends PersonnelDataManager {

    @Inject
    OwnerAdminsRepository ownerAdminsRepository;
    @Inject
    UserRepository userRepository;

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
}
