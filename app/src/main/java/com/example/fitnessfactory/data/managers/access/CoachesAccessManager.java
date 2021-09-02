package com.example.fitnessfactory.data.managers.access;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.repositories.access.CoachesAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;
public class CoachesAccessManager extends PersonnelAccessManager {

    @Inject
    CoachesAccessRepository coachesAccessRepository;
    @Inject
    OwnerCoachesRepository ownerCoachesRepository;

    public CoachesAccessManager() {
        FFApp.get().getAppComponent().inject(this);
    }

    @Override
    protected CoachesAccessRepository getAccessRepository() {
        return coachesAccessRepository;
    }

    @Override
    protected OwnerCoachesRepository getOwnerRepository() {
        return ownerCoachesRepository;
    }

    @Override
    protected String getAlreadyRegisteredMessage() {
        return ResUtils.getString(R.string.message_admin_is_registered);
    }
}
