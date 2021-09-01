package com.example.fitnessfactory.data.managers;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.repositories.CoachesAccessRepository;
import com.example.fitnessfactory.data.repositories.OwnerCoachesRepository;
import com.example.fitnessfactory.system.SafeReference;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.firebase.firestore.WriteBatch;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class CoachesAccessManager extends BaseManager {

    @Inject
    CoachesAccessRepository coachesAccessRepository;
    @Inject
    OwnerCoachesRepository ownerCoachesRepository;

    public CoachesAccessManager() {
        FFApp.get().getAppComponent().inject(this);
    }

    public Single<Boolean> createCoach(String ownerId, String userEmail) {
        SafeReference<WriteBatch> registerBatch = new SafeReference<>();

        return coachesAccessRepository.isCoachWithThisEmailRegisteredAsync(ownerId, userEmail)
                .flatMap(isRegistered ->
                        isRegistered ?
                                Single.error(new Exception(ResUtils.getString(R.string.message_admin_is_registered)))
                                : coachesAccessRepository.getRegisterCoachAccessEntryBatchAsync(ownerId, userEmail))
                .flatMap(writeBatch -> {
                    registerBatch.set(writeBatch);
                    return ownerCoachesRepository.isCoachWithThisEmailAddedAsync(userEmail);
                })
                .flatMap(isAdded ->
                        isAdded ?
                                Single.just(registerBatch.getValue()) :
                                ownerCoachesRepository.getAddCoachBatchAsync(registerBatch.getValue(), userEmail))
                .flatMap(this::commitBatchSingle);
    }

    public Completable deleteCoachCompletable(String ownerId, String email) {
        return getDeleteBatch(ownerId, email)
                .flatMapCompletable(this::commitBatchCompletable);
    }

    public Single<Boolean> deleteCoachSingle(String ownerId, String email) {
        return getDeleteBatch(ownerId, email)
                .flatMap(this::commitBatchSingle);
    }

    private Single<WriteBatch> getDeleteBatch(String ownerId, String email) {
        return coachesAccessRepository.getDeleteCoachAccessEntryBatchAsync(ownerId, email)
                .flatMap(writeBatch -> ownerCoachesRepository.getDeleteCoachBatchAsync(writeBatch, email));
    }
}
