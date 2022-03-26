package com.example.fitnessfactory.data.managers.access;

import com.example.fitnessfactory.data.managers.BaseManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.data.repositories.access.PersonnelAccessRepository;
import com.example.fitnessfactory.system.SafeReference;
import com.google.firebase.firestore.WriteBatch;

import io.reactivex.Completable;
import io.reactivex.Single;

public abstract class PersonnelAccessManager extends BaseManager {

    protected PersonnelAccessRepository accessRepository;

    protected OwnerPersonnelRepository ownerRepository;

    public PersonnelAccessManager(PersonnelAccessRepository accessRepository,
                                  OwnerPersonnelRepository ownerRepository) {
        this.accessRepository = accessRepository;
        this.ownerRepository = ownerRepository;
    }

    protected PersonnelAccessRepository getAccessRepository() {
        return accessRepository;
    }

    protected OwnerPersonnelRepository getOwnerRepository() {
        return ownerRepository;
    }

    protected abstract String getAlreadyRegisteredMessage();

    public Single<Boolean> createPersonnel(String ownerId, String userEmail) {
        SafeReference<WriteBatch> registerBatch = new SafeReference<>();

        return getAccessRepository().isPersonnelWithThisEmailRegisteredAsync(ownerId, userEmail)
                .flatMap(isRegistered ->
                        isRegistered ?
                                Single.error(new Exception(getAlreadyRegisteredMessage()))
                                : getAccessRepository().getRegisterPersonnelAccessEntryBatchAsync(ownerId, userEmail))
                .flatMap(writeBatch -> {
                    registerBatch.set(writeBatch);
                    return getOwnerRepository().isPersonnelWithThisEmailAddedAsync(userEmail);
                })
                .flatMap(isAdded ->
                        isAdded ?
                                Single.just(registerBatch.getValue()) :
                                getOwnerRepository().getAddPersonnelBatchAsync(registerBatch.getValue(), userEmail))
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

    protected Single<WriteBatch> getDeleteBatch(String ownerId, String personnelEmail) {
        return getAccessRepository().getDeletePersonnelAccessEntryBatchAsync(ownerId, personnelEmail)
                .flatMap(writeBatch -> getOwnerRepository().getDeletePersonnelBatchAsync(writeBatch, personnelEmail));
    }
}
