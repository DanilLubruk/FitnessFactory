package com.example.fitnessfactory.mockHelpers.mockers.data;

import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.CoachesDataProvider;

public class CoachesDataManagerMocker {

    public static CoachesDataManager createMock(OwnerCoachesRepository ownerRepository,
                                                UserRepository userRepository,
                                                OwnerGymRepository ownerGymRepository) {
        CoachesDataManager dataManager =
                new CoachesDataManager(ownerRepository, userRepository, ownerGymRepository);

        PersonnelDataManagerMocker.setupMock(
                new CoachesDataProvider(),
                ownerRepository,
                userRepository,
                ownerGymRepository);

        return dataManager;
    }
}
