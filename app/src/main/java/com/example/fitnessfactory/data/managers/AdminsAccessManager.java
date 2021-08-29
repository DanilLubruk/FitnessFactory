package com.example.fitnessfactory.data.managers;

import android.text.TextUtils;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.repositories.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.AdminsRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
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

    public Single<String> registerAdmin(Single<String> emailDialog, Single<Boolean> sendInvitationDialog) {
        AtomicReference<String> adminEmail = new AtomicReference<>();

        return emailDialog
                .subscribeOn(getMainThreadScheduler())
                .observeOn(getMainThreadScheduler())
                .flatMap(email -> {
                    email = email.toLowerCase();
                    adminEmail.set(email);
                    return Single.just(email);
                })
                .subscribeOn(getIOScheduler())
                .observeOn(getIOScheduler())
                .flatMap(email -> createAdmin(AppPrefs.gymOwnerId().getValue(), email))
                .flatMap(isCreated -> isCreated ? Single.just(AppPrefs.askForSendingAdminEmailInvite().getValue()) : Single.just(false))
                .subscribeOn(getMainThreadScheduler())
                .observeOn(getMainThreadScheduler())
                .flatMap(doAsk -> doAsk ? sendInvitationDialog : Single.just(false))
                .flatMap(doSendInvitation -> doSendInvitation ? Single.just(adminEmail.get()) : Single.just(StringUtils.getEmptyString()))
                .subscribeOn(getIOScheduler());
    }

    private Single<Boolean> createAdmin(String ownerId, String userEmail) {
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
                .flatMap(writeBatch -> adminsRepository.getDeleteAdminBatchAsync(writeBatch, adminEmail));
    }
}
