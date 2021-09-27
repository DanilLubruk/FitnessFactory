package com.example.fitnessfactory.mockHelpers.mockers.data;

import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.personnel.AdminsDataProvider;

public class AdminsDataManagerMocker {

    public static AdminsDataManager createMock(OwnerAdminsRepository ownerRepository,
                                               UserRepository userRepository,
                                               OwnerGymRepository ownerGymRepository) {
        AdminsDataManager dataManager =
                new AdminsDataManager(ownerRepository, userRepository, ownerGymRepository);

        PersonnelDataManagerMocker.setupMock(
                new AdminsDataProvider(),
                ownerRepository,
                userRepository,
                ownerGymRepository);

        return dataManager;
    }
}
