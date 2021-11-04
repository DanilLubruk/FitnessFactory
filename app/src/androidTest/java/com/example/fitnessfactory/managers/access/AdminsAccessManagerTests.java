package com.example.fitnessfactory.managers.access;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnessfactory.TestFFApp;
import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.access.PersonnelAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.data.repositories.ownerData.participantsData.ParticipantSessionsRepository;
import com.example.fitnessfactory.mockHelpers.mockers.access.AdminsAccessManagerMocker;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.inject.Inject;

@RunWith(AndroidJUnit4.class)
public class AdminsAccessManagerTests extends PersonnelAccessManagerTests {

    @Inject
    AdminsAccessRepository accessRepository;
    @Inject
    OwnerAdminsRepository ownerRepository;
    @Inject
    AdminsAccessManager adminsAccessManager;

    @Before
    public void setup() {
        super.setup();
        TestFFApp.getTestAppComponent().inject(this);
    }

    @Override
    protected PersonnelAccessRepository getAccessRepository() {
        return accessRepository;
    }

    @Override
    protected OwnerPersonnelRepository getOwnersRepository() {
        return ownerRepository;
    }

    @Override
    protected PersonnelAccessManager getPersonnelAccessManager() {
        return adminsAccessManager;
    }


}
