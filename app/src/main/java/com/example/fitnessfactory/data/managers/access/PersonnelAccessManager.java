package com.example.fitnessfactory.data.managers.access;

import com.example.fitnessfactory.data.managers.BaseManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.data.repositories.access.PersonnelAccessRepository;
import com.example.fitnessfactory.system.SafeReference;
import com.google.firebase.firestore.WriteBatch;

import io.reactivex.Completable;
import io.reactivex.Single;

public abstract class PersonnelAccessManager extends BaseManager {

    protected abstract PersonnelAccessRepository getAccessRepository();

    protected abstract OwnerPersonnelRepository getOwnerRepository();

    protected abstract String getAlreadyRegisteredMessage();

    public Single<Boolean> createPersonnel(String ownerId, String userEmail) {
        SafeReference<WriteBatch> registerBatch = new SafeReference<>();

        return getAccessRepository().isPersonnelWithThisEmailRegistered(ownerId, userEmail)
                .flatMap(isRegistered ->
                        isRegistered ?
                                Single.error(new Exception(getAlreadyRegisteredMessage()))
                                : getAccessRepository().getRegisterPersonnelAccessEntryBatch(ownerId, userEmail))
                .flatMap(writeBatch -> {
                    registerBatch.set(writeBatch);
                    return getOwnerRepository().isPersonnelWithThisEmailAdded(userEmail);
                })
                .flatMap(isAdded ->
                        isAdded ?
                                Single.just(registerBatch.getValue()) :
                                getOwnerRepository().getAddPersonnelBatch(registerBatch.getValue(), userEmail))
                .flatMap(this::commitBatchSingle);
    }

    public Single<Boolean> deletePersonnelSingle(String ownerId, String personnelEmail) {
        return getDeleteBatch(ownerId, personnelEmail)
                .flatMap(this::commitBatchSingle);
    }

    public Completable deletePersonnelCompletable(String ownerId, String personnelEmail) {
        return getDeleteBatch(ownerId, personnelEmail)
                .flatMapCompletable(this::commitBatchCompletable);
    }

    private Single<WriteBatch> getDeleteBatch(String ownerId, String personnelEmail) {
        return getAccessRepository().getDeletePersonnelAccessEntryBatch(ownerId, personnelEmail)
                .flatMap(writeBatch -> getOwnerRepository().getDeletePersonnelBatch(writeBatch, personnelEmail));
    }
}
