package com.example.fitnessfactory.managers.access;

import com.example.fitnessfactory.BaseTests;
import com.example.fitnessfactory.data.managers.access.GymsAccessManager;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.mockHelpers.mockers.OwnerGymRepositoryMocker;
import com.example.fitnessfactory.mockHelpers.mockers.access.GymAccessManagerMocker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

public class GymAccessManagerTests extends BaseTests {

    private SessionsRepository sessionsRepository = Mockito.mock(SessionsRepository.class);

    private OwnerGymRepository mockedOwnerGymRepository =
            OwnerGymRepositoryMocker.createMocker(Mockito.mock(OwnerGymRepository.class));

    private OwnerAdminsRepository ownerAdminsRepository = Mockito.mock(OwnerAdminsRepository.class);

    private OwnerCoachesRepository ownerCoachesRepository = Mockito.mock(OwnerCoachesRepository.class);

    private GymsAccessManager accessManager;

    @Before
    public void setup() {
        super.setup();
        accessManager =
                GymAccessManagerMocker
                        .createMocker(
                                sessionsRepository,
                                mockedOwnerGymRepository,
                                ownerAdminsRepository,
                                ownerCoachesRepository);
    }

    @Test
    public void deleteGymTest() {
        WriteBatch writeBatch = FirebaseFirestore.getInstance().batch();

        Mockito.when(mockedOwnerGymRepository.getDeleteGymBatchAsync(Mockito.anyString()))
                .thenReturn(Single.just(writeBatch));

        Mockito.when(ownerAdminsRepository.getRemoveGymFromPersonnelBatchAsync(Mockito.any(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    WriteBatch argWriteBatch = invocation.getArgument(0);

                    if (argWriteBatch.equals(writeBatch)) {
                        return Single.just(argWriteBatch);
                    } else {
                        return Single.error(new Exception());
                    }
                });

        Mockito.when(ownerCoachesRepository.getRemoveGymFromPersonnelBatchAsync(Mockito.any(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    WriteBatch argWriteBatch = invocation.getArgument(0);

                    if (argWriteBatch.equals(writeBatch)) {
                        return Single.just(argWriteBatch);
                    } else {
                        return Single.error(new Exception());
                    }
                });

        TestObserver<Boolean> subscriber =
                subscribeInTestThread(accessManager.deleteGymSingle(Gym.builder().setName("gymId2").build()));

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        boolean isDeleted = subscriber.values().get(0);
        assertTrue(isDeleted);
        subscriber.dispose();

        Mockito.verify(mockedOwnerGymRepository).getDeleteGymBatchAsync("gymId2");
        Mockito.verify(ownerAdminsRepository).getRemoveGymFromPersonnelBatchAsync(writeBatch, "gymId2");
        Mockito.verify(ownerCoachesRepository).getRemoveGymFromPersonnelBatchAsync(writeBatch, "gymId2");
    }
}
