package com.example.fitnessfactory.mockHelpers.mockers.access;

import com.example.fitnessfactory.data.managers.access.GymsAccessManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.mockHelpers.mockers.OwnerGymRepositoryMocker;

public class GymAccessManagerMocker {

    public static GymsAccessManager createMocker(OwnerGymRepository ownerGymRepository,
                                                 OwnerAdminsRepository ownerAdminsRepository,
                                                 OwnerCoachesRepository ownerCoachesRepository) {
        return new GymsAccessManager(
                        OwnerGymRepositoryMocker.createMocker(ownerGymRepository),
                        ownerAdminsRepository,
                        ownerCoachesRepository);
        /*Mockito.when(ownerGymRepository.getDeleteGymBatchAsync(Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        Mockito.when(ownerAdminsRepository.getRemoveGymFromAdminBatchAsync(Mockito.any(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));*/
    }
}
