package com.example.fitnessfactory.managers.access;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.repositories.access.CoachesAccessRepository;
import com.example.fitnessfactory.data.repositories.access.PersonnelAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.mockHelpers.mockers.CoachesAccessManagerMocker;

import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class CoachesAccessManagerTests extends PersonnelAccessManagerTests {

    private CoachesAccessRepository accessRepository = Mockito.mock(CoachesAccessRepository.class);

    private OwnerCoachesRepository ownerRepository = Mockito.mock(OwnerCoachesRepository.class);

    @Override
    protected PersonnelAccessManager instantiateAccessManager() {
        return CoachesAccessManagerMocker.createMock(accessRepository, ownerRepository);
    }

    @Override
    protected PersonnelAccessRepository getAccessRepository() {
        return accessRepository;
    }

    @Override
    protected OwnerPersonnelRepository getOwnersRepository() {
        return ownerRepository;
    }
}
