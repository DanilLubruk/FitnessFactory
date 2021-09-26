package com.example.fitnessfactory.data.managers.access;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class AdminsAccessManager extends PersonnelAccessManager {

    @Inject
    public AdminsAccessManager(AdminsAccessRepository accessRepository,
                               OwnerAdminsRepository ownerRepository) {
        super(accessRepository, ownerRepository);
    }

    @Override
    protected String getAlreadyRegisteredMessage() {
        return ResUtils.getString(R.string.message_admin_is_registered);
    }
}
