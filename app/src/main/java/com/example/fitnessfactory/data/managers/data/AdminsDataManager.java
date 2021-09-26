package com.example.fitnessfactory.data.managers.data;

import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;

import javax.inject.Inject;

public class AdminsDataManager extends PersonnelDataManager {

    @Inject
    public AdminsDataManager(OwnerAdminsRepository ownerRepository,
                             UserRepository userRepository,
                             OwnerGymRepository gymRepository) {
        super(ownerRepository, userRepository, gymRepository);
    }
}
