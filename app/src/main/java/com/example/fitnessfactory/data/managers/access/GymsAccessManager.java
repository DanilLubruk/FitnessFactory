package com.example.fitnessfactory.data.managers.access;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.managers.BaseManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.google.firebase.firestore.WriteBatch;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class GymsAccessManager extends BaseManager {

    private OwnerGymRepository ownerGymRepository;
    private OwnerAdminsRepository ownerAdminsRepository;
    private OwnerCoachesRepository ownerCoachesRepository;

    @Inject
    public GymsAccessManager(OwnerGymRepository ownerGymRepository,
                             OwnerAdminsRepository ownerAdminsRepository,
                             OwnerCoachesRepository ownerCoachesRepository) {
        this.ownerGymRepository = ownerGymRepository;
        this.ownerAdminsRepository = ownerAdminsRepository;
        this.ownerCoachesRepository = ownerCoachesRepository;
    }

    public Single<Boolean> deleteGymSingle(String gymId) {
        return getDeleteBatch(gymId)
                .flatMap(this::commitBatchSingle);
    }

    public Completable deleteGymCompletable(String gymId) {
        return getDeleteBatch(gymId)
                .flatMapCompletable(this::commitBatchCompletable);
    }

    private Single<WriteBatch> getDeleteBatch(String gymId) {
        return ownerGymRepository.getDeleteGymBatchAsync(gymId)
                .flatMap(deleteBatch ->
                        ownerAdminsRepository.getRemoveGymFromAdminBatchAsync(deleteBatch, gymId))
                .flatMap(deleteBatch ->
                        ownerCoachesRepository.getRemoveGymFromCoachBatchAsync(deleteBatch, gymId));
    }
}
