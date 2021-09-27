package com.example.fitnessfactory.mockHelpers.mockers.access;

import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.AdminsDataProvider;

public class AdminsAccessManagerMocker {

    public static AdminsAccessManager createMock(AdminsAccessRepository accessRepository,
                                                 OwnerAdminsRepository ownersRepository) {
        AdminsAccessManager adminsAccessManager =
                new AdminsAccessManager(accessRepository, ownersRepository);

        PersonnelAccessManagerMocker.setupMock(
                new AdminsDataProvider(),
                accessRepository,
                ownersRepository);

        return adminsAccessManager;
    }
}
