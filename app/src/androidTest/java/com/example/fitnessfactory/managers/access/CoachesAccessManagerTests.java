package com.example.fitnessfactory.managers.access;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnessfactory.TestFFApp;
import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.repositories.access.CoachesAccessRepository;
import com.example.fitnessfactory.data.repositories.access.PersonnelAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.data.repositories.ownerData.participantsData.CoachSessionsRepository;
import com.example.fitnessfactory.mockHelpers.mockers.access.CoachesAccessManagerMocker;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.inject.Inject;

import io.reactivex.Single;

@RunWith(AndroidJUnit4.class)
public class CoachesAccessManagerTests extends PersonnelAccessManagerTests {

    @Inject
    CoachSessionsRepository coachSessionsRepository;
    @Inject
    CoachesAccessRepository accessRepository;
    @Inject
    OwnerCoachesRepository ownerRepository;
    @Inject
    CoachesAccessManager coachesAccessManager;

    @Before
    public void setup() {
        super.setup();
        TestFFApp.getTestAppComponent().inject(this);
        Mockito.when(coachSessionsRepository.isParticipantOccupiedAsync(Mockito.anyString())).thenReturn(Single.just(false));
    }

    @Override
    protected PersonnelAccessManager getPersonnelAccessManager() {
        return coachesAccessManager;
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
