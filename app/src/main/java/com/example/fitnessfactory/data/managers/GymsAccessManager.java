package com.example.fitnessfactory.data.managers;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.repositories.AdminsRepository;
import com.example.fitnessfactory.data.repositories.GymRepository;
import com.google.firebase.firestore.WriteBatch;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class GymsAccessManager extends BaseManager {

    @Inject
    GymRepository gymRepository;
    @Inject
    AdminsRepository adminsRepository;

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
        return gymRepository.getDeleteGymBatchAsync(gymId)
                .flatMap(deleteBatch ->
                        adminsRepository.getRemoveGymFromAdminBatchAsync(deleteBatch, gymId));
    }
}
