package com.example.fitnessfactory.mockHelpers.mockers.data;

import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.personnel.PersonnelDataProvider;
import com.example.fitnessfactory.mockHelpers.mockers.OwnerGymRepositoryMocker;

public class AdminsDataManagerMocker {

    public static AdminsDataManager createMock(OwnerAdminsRepository ownerRepository,
                                               UserRepository userRepository,
                                               OwnerGymRepository ownerGymRepository) {
        AdminsDataManager dataManager =
                new AdminsDataManager(
                        ownerRepository,
                        userRepository,
                        OwnerGymRepositoryMocker.createMocker(ownerGymRepository));

        PersonnelDataManagerMocker.setupMock(
                new PersonnelDataProvider(),
                ownerRepository,
                userRepository);

        return dataManager;
    }
}
