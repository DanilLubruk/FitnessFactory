package com.example.fitnessfactory.mockHelpers.mockers.data;

import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.personnel.PersonnelDataProvider;
import com.example.fitnessfactory.mockHelpers.mockers.OwnerGymRepositoryMocker;

public class CoachesDataManagerMocker {

    public static CoachesDataManager createMock(OwnerCoachesRepository ownerRepository,
                                                UserRepository userRepository,
                                                OwnerGymRepository ownerGymRepository) {
        CoachesDataManager dataManager =
                new CoachesDataManager(
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
