package com.example.fitnessfactory.managers.data;

import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.mockHelpers.mockers.CoachesDataManagerMocker;

import org.mockito.Mockito;

public class CoachesDataManagerTests extends PersonnelDataManagerTests {

    private OwnerCoachesRepository ownerRepository = Mockito.mock(OwnerCoachesRepository.class);

    @Override
    protected PersonnelDataManager instantiateDataManager() {
        return CoachesDataManagerMocker.createMock(ownerRepository, getUserRepository(), getGymRepository());
    }

    @Override
    protected OwnerPersonnelRepository getOwnerRepository() {
        return ownerRepository;
    }
}
