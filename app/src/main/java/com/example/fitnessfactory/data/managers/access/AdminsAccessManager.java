package com.example.fitnessfactory.data.managers.access;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.repositories.access.PersonnelAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class AdminsAccessManager extends PersonnelAccessManager {

    @Inject
    public AdminsAccessManager(PersonnelAccessRepository accessRepository,
                               OwnerPersonnelRepository ownerRepository) {
        super(accessRepository, ownerRepository);
    }

    @Override
    protected String getAlreadyRegisteredMessage() {
        return ResUtils.getString(R.string.message_admin_is_registered);
    }
}
