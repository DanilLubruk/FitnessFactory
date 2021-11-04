package com.example.fitnessfactory.mockHelpers.mockers.access;

import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.personnel.PersonnelDataProvider;

public class AdminsAccessManagerMocker {

    public static AdminsAccessManager createMock(AdminsAccessRepository accessRepository,
                                                 OwnerAdminsRepository ownersRepository) {
        AdminsAccessManager adminsAccessManager =
                new AdminsAccessManager(accessRepository, ownersRepository);

        PersonnelAccessManagerMocker.setupMock(
                new PersonnelDataProvider(),
                accessRepository,
                ownersRepository);

        return adminsAccessManager;
    }
}
