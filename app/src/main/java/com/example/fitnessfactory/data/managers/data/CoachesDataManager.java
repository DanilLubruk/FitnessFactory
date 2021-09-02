package com.example.fitnessfactory.data.managers.data;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;


import javax.inject.Inject;

public class CoachesDataManager extends PersonnelDataManager {

    @Inject
    OwnerCoachesRepository ownerCoachesRepository;
    @Inject
    UserRepository userRepository;

    public CoachesDataManager() {
        FFApp.get().getAppComponent().inject(this);
    }

    @Override
    protected OwnerCoachesRepository getOwnerRepository() {
        return ownerCoachesRepository;
    }

    @Override
    protected UserRepository getUserRepository() {
        return userRepository;
    }
}
