package com.example.fitnessfactory.data.managers.access;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.repositories.access.CoachesAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class CoachesAccessManager extends PersonnelAccessManager {

    @Inject
    public CoachesAccessManager(CoachesAccessRepository accessRepository,
                                OwnerCoachesRepository ownerRepository) {
        super(accessRepository, ownerRepository);
    }

    @Override
    protected String getAlreadyRegisteredMessage() {
        return ResUtils.getString(R.string.message_admin_is_registered);
    }
}
