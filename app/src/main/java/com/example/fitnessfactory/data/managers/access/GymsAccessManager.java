package com.example.fitnessfactory.data.managers.access;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.managers.BaseManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.google.firebase.firestore.WriteBatch;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class GymsAccessManager extends BaseManager {

    @Inject
    OwnerGymRepository ownerGymRepository;
    @Inject
    OwnerAdminsRepository ownerAdminsRepository;

    public GymsAccessManager() {
        FFApp.get().getAppComponent().inject(this);
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
                        ownerAdminsRepository.getRemoveGymFromAdminBatchAsync(deleteBatch, gymId));
    }
}
