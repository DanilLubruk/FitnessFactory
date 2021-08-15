package com.example.fitnessfactory.data.managers;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.repositories.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.AdminsRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.firebase.firestore.WriteBatch;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class AdminsAccessManager extends BaseManager {

    @Inject
    AdminsAccessRepository accessRepository;
    @Inject
    AdminsRepository adminsRepository;

    public AdminsAccessManager() {
        FFApp.get().getAppComponent().inject(this);
    }

    public Completable createAdmin(String ownerId, String userEmail) {
        AtomicReference<WriteBatch> registerBatch = new AtomicReference<>();

        return accessRepository.isAdminWithThisEmailRegisteredAsync(userEmail)
                .flatMap(isRegistered ->
                        isRegistered ?
                                Single.error(new Exception(ResUtils.getString(R.string.message_admin_is_registered)))
                                : accessRepository.getRegisterAdminAccessEntryBatchAsync(ownerId, userEmail))
                .flatMap(writeBatch -> {
                    registerBatch.set(writeBatch);
                    return adminsRepository.isAdminWithThisEmailAddedAsync(userEmail);
                })
                .flatMap(isAdded ->
                        isAdded ?
                                Single.just(registerBatch.get()) :
                                adminsRepository.getAddAdminBatchAsync(userEmail, registerBatch.get()))
                .flatMapCompletable(this::commitBatchCompletable);
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
                .flatMap(writeBatch -> adminsRepository.getDeleteAdminBatchAsync(writeBatch, adminEmail));
    }
}
