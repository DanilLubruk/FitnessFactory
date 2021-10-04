package com.example.fitnessfactory.data.managers;

import android.content.Intent;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.OrganisationInfoRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.system.FirebaseAuthManager;
import com.example.fitnessfactory.system.SafeReference;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class AuthManager extends BaseManager {

    UserRepository userRepository;
    AdminsAccessRepository adminsAccessRepository;
    FirebaseAuthManager authManager;
    OrganisationInfoRepository organisationInfoRepository;

    @Inject
    public AuthManager(UserRepository userRepository,
                       AdminsAccessRepository adminsAccessRepository,
                       FirebaseAuthManager authManager,
                       OrganisationInfoRepository organisationInfoRepository) {
        this.userRepository = userRepository;
        this.adminsAccessRepository = adminsAccessRepository;
        this.authManager = authManager;
        this.organisationInfoRepository = organisationInfoRepository;
    }

    public Single<List<AppUser>> handleSignIn(Intent signInData) {
        SafeReference<AppUser> user = new SafeReference<>();

        return authManager.handleSignInResult(signInData)
                .observeOn(getIOScheduler())
                .subscribeOn(getIOScheduler())
                .flatMap(authUser -> {
                    user.set(authUser);
                    return userRepository.isUserRegisteredAsync(authUser.getEmail());
                })
                .observeOn(getIOScheduler())
                .flatMap(isRegistered ->
                        isRegistered ?
                                userRepository.getAppUserByEmailAsync(user.getValue().getEmail()) :
                                userRepository.registerUser(
                                        user.getValue().getEmail(),
                                        user.getValue().getName()))
                .observeOn(getIOScheduler())
                .flatMap(authUser -> adminsAccessRepository.getOwnersByInvitedEmail(authUser))
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
