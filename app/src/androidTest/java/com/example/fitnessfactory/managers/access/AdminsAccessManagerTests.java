package com.example.fitnessfactory.managers.access;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.access.PersonnelAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.mockHelpers.mockers.AdminsAccessManagerMocker;

import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class AdminsAccessManagerTests extends PersonnelAccessManagerTests {

    private AdminsAccessRepository accessRepository = Mockito.mock(AdminsAccessRepository.class);

    private OwnerAdminsRepository ownerRepository = Mockito.mock(OwnerAdminsRepository.class);

    @Override
    protected PersonnelAccessRepository getAccessRepository() {
        return accessRepository;
    }

    @Override
    protected OwnerPersonnelRepository getOwnersRepository() {
        return ownerRepository;
    }

    @Override
    protected PersonnelAccessManager instantiateAccessManager() {

        return AdminsAccessManagerMocker.createMock(accessRepository, ownerRepository);
    }
}
