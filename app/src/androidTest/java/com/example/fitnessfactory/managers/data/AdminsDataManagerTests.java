package com.example.fitnessfactory.managers.data;

import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.mockHelpers.mockers.data.AdminsDataManagerMocker;

import org.mockito.Mockito;

public class AdminsDataManagerTests extends PersonnelDataManagerTests {

    private OwnerAdminsRepository ownerRepository = Mockito.mock(OwnerAdminsRepository.class);

    @Override
    protected PersonnelDataManager instantiateDataManager() {
        return AdminsDataManagerMocker.createMock(ownerRepository, getUserRepository(), getGymRepository());
    }

    @Override
    protected OwnerPersonnelRepository getOwnerRepository() {
        return ownerRepository;
    }
}
