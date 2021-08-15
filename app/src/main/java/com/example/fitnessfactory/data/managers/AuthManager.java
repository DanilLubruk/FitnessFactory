package com.example.fitnessfactory.data.managers;

import android.content.Intent;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.OrganisationInfoRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.system.FirebaseAuthManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class AuthManager extends BaseManager {

    @Inject
    UserRepository userRepository;
    @Inject
    AdminsAccessRepository adminsAccessRepository;
    @Inject
    FirebaseAuthManager authManager;
    @Inject
    OrganisationInfoRepository organisationInfoRepository;

    public AuthManager() {
        FFApp.get().getAppComponent().inject(this);
    }

    public Single<List<AppUser>> handleSignIn(Intent signInData) {
        AtomicReference<String> userEmail = new AtomicReference<>();

        return authManager.handleSignInResult(signInData)
                .observeOn(getIOScheduler())
                .subscribeOn(getIOScheduler())
                .flatMap(email -> {
                    userEmail.set(email);
                    return userRepository.isRegistered(email);
                })
                .observeOn(getIOScheduler())
                .flatMap(isRegistered ->
                        isRegistered ?
                                userRepository.getAppUserByEmailAsync(userEmail.get()) :
                                userRepository.registerUser(
                                        userEmail.get(),
                                        FirebaseAuthManager.getCurrentUserName()))
                .observeOn(getIOScheduler())
                .flatMap(user -> adminsAccessRepository.getOwnersByInvitedEmail(user))
                .observeOn(getIOScheduler())
                .flatMap(ownersIds -> userRepository.getOwnersByIds(ownersIds))
                .observeOn(getMainThreadScheduler());
    }

    public Completable checkOrganisationName() {
        return organisationInfoRepository.getOrganisationNameAsync()
                .subscribeOn(getIOScheduler())
                .observeOn(getIOScheduler())
                .flatMapCompletable(organisationInfoRepository::checkOrganisationNameAsync)
                .observeOn(getMainThreadScheduler());
    }
}
