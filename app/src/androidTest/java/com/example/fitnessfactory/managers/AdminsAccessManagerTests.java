package com.example.fitnessfactory.managers;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.repositories.access.PersonnelAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.mockHelpers.AdminsAccessManagerMocker;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AdminsAccessManagerTests extends PersonnelAccessManagerTests {

    @Override
    protected PersonnelAccessManager instantiateAccessManager(PersonnelAccessRepository personnelAccessRepository,
                                                              OwnerPersonnelRepository ownersRepository) {
        return AdminsAccessManagerMocker.createMock(personnelAccessRepository, ownersRepository);
    }
}
