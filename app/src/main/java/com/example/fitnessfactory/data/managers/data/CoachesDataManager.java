package com.example.fitnessfactory.data.managers.data;

import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;

import javax.inject.Inject;

public class CoachesDataManager extends PersonnelDataManager {

    @Inject
    public CoachesDataManager(OwnerCoachesRepository ownerRepository,
                              UserRepository userRepository,
                              OwnerGymRepository gymRepository) {
        super(ownerRepository, userRepository, gymRepository);
    }
}
