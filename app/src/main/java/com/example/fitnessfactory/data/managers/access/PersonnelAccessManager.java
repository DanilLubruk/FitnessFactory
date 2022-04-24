package com.example.fitnessfactory.data.managers.access;

import com.example.fitnessfactory.data.managers.BaseManager;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.data.repositories.access.PersonnelAccessRepository;
import com.example.fitnessfactory.system.SafeReference;
import com.google.firebase.firestore.WriteBatch;

import io.reactivex.Completable;
import io.reactivex.Single;

public abstract class PersonnelAccessManager extends BaseManager {

    protected PersonnelAccessRepository accessRepository;

    protected OwnerPersonnelRepository ownerRepository;

    protected UserRepository userRepository;

    public PersonnelAccessManager(PersonnelAccessRepository accessRepository,
                                  OwnerPersonnelRepository ownerRepository,
                                  UserRepository userRepository) {
        this.accessRepository = accessRepository;
        this.ownerRepository = ownerRepository;
        this.userRepository = userRepository;
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
        SafeReference<String> userIdRef = new SafeReference<>();

        return userRepository.getUserIdByEmail(userEmail)
                .flatMap(userId -> {
                    userIdRef.set(userId);
                    return getAccessRepository().isPersonnelWithThisIdRegisteredAsync(ownerId, userId);
                })
                .flatMap(isRegistered ->
                        isRegistered ?
                                Single.error(new Exception(getAlreadyRegisteredMessage()))
                                : getAccessRepository().getRegisterPersonnelAccessEntryBatchAsync(ownerId, userIdRef.getValue()))
                .flatMap(writeBatch -> {
                    registerBatch.set(writeBatch);
                    return getOwnerRepository().isPersonnelWithThisIdAddedAsync(userIdRef.getValue());
                })
                .flatMap(isAdded ->
                        isAdded ?
                                Single.just(registerBatch.getValue()) :
                                getOwnerRepository().getAddPersonnelBatchAsync(registerBatch.getValue(), userIdRef.getValue()))
                .flatMap(this::commitBatchSingle);
    }

    public Single<Boolean> deletePersonnelSingle(String ownerId, String userId) {
        return getDeleteBatch(ownerId, userId)
                .flatMap(this::commitBatchSingle);
    }

    public Completable deletePersonnelCompletable(String ownerId, String userId) {
        return getDeleteBatch(ownerId, userId)
                .flatMapCompletable(this::commitBatchCompletable);
    }

    protected Single<WriteBatch> getDeleteBatch(String ownerId, String userId) {
        return getAccessRepository().getDeletePersonnelAccessEntryBatchAsync(ownerId, userId)
                .flatMap(writeBatch -> getOwnerRepository().getDeletePersonnelBatchAsync(writeBatch, userId));
    }
}
