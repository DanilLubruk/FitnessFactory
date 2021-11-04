package com.example.fitnessfactory.mockHelpers.mockers.access;

import com.example.fitnessfactory.data.managers.access.GymsAccessManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.mockHelpers.mockers.OwnerGymRepositoryMocker;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mockito.Mockito;

import io.reactivex.Single;

public class GymAccessManagerMocker {

    public static GymsAccessManager createMocker(SessionsRepository sessionsRepository,
                                                 OwnerGymRepository ownerGymRepository,
                                                 OwnerAdminsRepository ownerAdminsRepository,
                                                 OwnerCoachesRepository ownerCoachesRepository) {
        GymsAccessManager accessManager =
                new GymsAccessManager(
                        sessionsRepository,
                        OwnerGymRepositoryMocker.createMocker(ownerGymRepository),
                        ownerAdminsRepository,
                        ownerCoachesRepository);

        Mockito.when(ownerGymRepository.getDeleteGymBatchAsync(Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        Mockito.when(ownerAdminsRepository.getRemoveGymFromPersonnelBatchAsync(Mockito.any(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        Mockito.when(ownerCoachesRepository.getRemoveGymFromPersonnelBatchAsync(Mockito.any(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        return accessManager;
    }
}
