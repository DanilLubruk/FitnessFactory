package com.example.fitnessfactory.data.managers;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.repositories.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.AdminsRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.firebase.firestore.WriteBatch;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import io.reactivex.Single;

public class AdminsAccessManager extends BaseManager {

    @Inject
    AdminsAccessRepository accessRepository;
    @Inject
    AdminsRepository adminsRepository;

    public AdminsAccessManager() {
        FFApp.get().getAppComponent().inject(this);
    }

    public void createAdmin(String ownerId, String userEmail) {
        AtomicReference<WriteBatch> registerBatch = new AtomicReference<>();

        addSubscription(accessRepository.isAdminRegisteredAsync(userEmail)
                .subscribeOn(getIOScheduler())
                .observeOn(getIOScheduler())
                .flatMap(isRegistered ->
                        isRegistered ?
                        Single.error(new Exception(ResUtils.getString(R.string.message_admin_is_registered)))
                        : accessRepository.registerAdminAccessEntryAsync(ownerId, userEmail))
                .flatMap(writeBatch -> {
                    registerBatch.set(writeBatch);
                    return adminsRepository.isAdminAddedAsync(userEmail);
                })
                .flatMap(isAdded ->
                        isAdded ?
                        Single.just(registerBatch.get()) :
                        adminsRepository.addAdminAsync(userEmail, registerBatch.get()))
                .flatMapCompletable(this::commitBatchCompletable)
                .subscribe(
                        () -> {
                        },
                        this::handleError));
    }

    public boolean deleteAdmin(String ownerId, String adminEmail) {
        AtomicReference<Boolean> isDeleted = new AtomicReference<>();

        addSubscription(accessRepository.deleteAdminAccessEntryAsync(ownerId, adminEmail)
        .subscribeOn(getIOScheduler())
        .observeOn(getIOScheduler())
        .flatMap(writeBatch -> adminsRepository.deleteAdminAsync(writeBatch, adminEmail))
        .flatMap(this::commitBatchSingle)
        .subscribe(
                isDeleted::set,
                throwable -> handleError(isDeleted, throwable)));

        return isDeleted.get();
    }
}
