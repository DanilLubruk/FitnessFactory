package com.example.fitnessfactory.data.managers;
import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.repositories.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.OwnerAdminsRepository;
import com.example.fitnessfactory.system.SafeReference;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.firebase.firestore.WriteBatch;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class AdminsAccessManager extends BaseManager {

    @Inject
    AdminsAccessRepository accessRepository;
    @Inject
    OwnerAdminsRepository ownerAdminsRepository;

    public AdminsAccessManager() {
        FFApp.get().getAppComponent().inject(this);
    }

    public Single<Boolean> createAdmin(String ownerId, String userEmail) {
        SafeReference<WriteBatch> registerBatch = new SafeReference<>();

        return accessRepository.isAdminWithThisEmailRegisteredAsync(ownerId, userEmail)
                .flatMap(isRegistered ->
                        isRegistered ?
                                Single.error(new Exception(ResUtils.getString(R.string.message_admin_is_registered)))
                                : accessRepository.getRegisterAdminAccessEntryBatchAsync(ownerId, userEmail))
                .flatMap(writeBatch -> {
                    registerBatch.set(writeBatch);
                    return ownerAdminsRepository.isAdminWithThisEmailAddedAsync(userEmail);
                })
                .flatMap(isAdded ->
                        isAdded ?
                                Single.just(registerBatch.getValue()) :
                                ownerAdminsRepository.getAddAdminBatchAsync(registerBatch.getValue(), userEmail))
                .flatMap(this::commitBatchSingle);
    }

    public Single<Boolean> deleteAdminSingle(String ownerId, String adminEmail) {
        return getDeleteBatch(ownerId, adminEmail)
                .flatMap(this::commitBatchSingle);
    }

    public Completable deleteAdminCompletable(String ownerId, String adminEmail) {
        return getDeleteBatch(ownerId, adminEmail)
                .flatMapCompletable(this::commitBatchCompletable);
    }

    private Single<WriteBatch> getDeleteBatch(String ownerId, String adminEmail) {
        return accessRepository.getDeleteAdminAccessEntryBatchAsync(ownerId, adminEmail)
                .flatMap(writeBatch -> ownerAdminsRepository.getDeleteAdminBatchAsync(writeBatch, adminEmail));
    }
}
