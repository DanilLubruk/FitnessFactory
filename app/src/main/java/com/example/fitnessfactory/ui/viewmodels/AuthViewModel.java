package com.example.fitnessfactory.ui.viewmodels;

import android.content.Intent;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.OrganisationInfoRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.system.FirebaseAuthManager;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.RxUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

public class AuthViewModel extends BaseViewModel {

    @Inject
    UserRepository userRepository;
    @Inject
    AdminsAccessRepository adminsAccessRepository;
    @Inject
    FirebaseAuthManager authManager;
    @Inject
    OrganisationInfoRepository organisationInfoRepository;


    public AuthViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public SingleLiveEvent<Intent> getSignInIntent() {
        SingleLiveEvent<Intent> observer = new SingleLiveEvent<>();

        subscribeInIOThread(authManager.getSignInIntentAsync(),
                new SingleData<>(
                        observer::setValue,
                        RxUtils::handleError));

        return observer;
    }

    public void interruptSignIn() {
        subscribeInIOThread(authManager.interruptSignInAsync(),
                RxUtils::handleError);
    }

    public SingleLiveEvent<List<AppUser>> handleSignIn(Intent authData) {
        SingleLiveEvent<List<AppUser>> observer = new SingleLiveEvent<>();

        AtomicReference<String> userEmail = new AtomicReference<>();
        addSubscription(authManager.handleSignInResult(authData)
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
                .observeOn(getMainThreadScheduler())
                .subscribe(observer::setValue,
                        throwable -> handleAuthError(throwable, observer)));

        return observer;
    }

    public void signOut() {
        subscribeInIOThread(authManager.signOutCompletable(), RxUtils::handleError);
    }

    public SingleLiveEvent<Boolean> checkOrganisationName() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

        addSubscription(organisationInfoRepository.getOrganisationNameAsync()
                .subscribeOn(getIOScheduler())
                .observeOn(getIOScheduler())
                .flatMapCompletable(organisationInfoRepository::checkOrganisationNameAsync)
                .observeOn(getMainThreadScheduler())
                .subscribe(() -> {
                    observer.setValue(true);
                }, throwable -> RxUtils.handleError(observer, throwable)));

        return observer;
    }

    private void handleAuthError(Throwable throwable, SingleLiveEvent<List<AppUser>> observer) {
        observer.setValue(null);
        throwable.printStackTrace();
        String message = ResUtils.getString(R.string.caption_wrong_auth)
                .concat(" - ")
                .concat(throwable.getLocalizedMessage());
        GuiUtils.showMessage(message);
    }
}
