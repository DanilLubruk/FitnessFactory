package com.example.fitnessfactory.ui.viewmodels;

import android.content.Intent;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.StaffAccessRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.system.FirebaseAuthManager;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import io.reactivex.Single;

public class AuthViewModel extends BaseViewModel {

    @Inject
    UserRepository userRepository;
    @Inject
    StaffAccessRepository accessRepository;
    @Inject
    FirebaseAuthManager authManager;


    public AuthViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public SingleLiveEvent<Intent> getSignInIntent() {
        SingleLiveEvent<Intent> observer = new SingleLiveEvent<>();

        subscribeInIOThread(authManager.getSignInIntentAsync(),
                new SingleData<>(
                        observer::setValue,
                        throwable -> {
                            throwable.printStackTrace();
                            GuiUtils.showMessage(throwable.getLocalizedMessage());
                        }));

        return observer;
    }

    public void interruptSignIn() {
        subscribeInIOThread(authManager.interruptSignInAsync(),
                throwable -> {
                    throwable.printStackTrace();
                    GuiUtils.showMessage(throwable.getLocalizedMessage());
                });
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
                .flatMap(user -> accessRepository.getOwnersByInvitedEmail(user))
                .observeOn(getIOScheduler())
                .flatMap(ownersIds -> userRepository.getOwnersByIds(ownersIds))
                .observeOn(getMainThreadScheduler())
                .subscribe(observer::setValue, throwable -> handleAuthError(throwable, observer)));

        return observer;
    }

    public SingleLiveEvent<Boolean> isRegistered(String email) {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

        subscribeInIOThread(userRepository.isRegistered(email),
                new SingleData<>(
                        observer::setValue,
                        throwable -> handleIsRegisteredError(throwable, observer)));

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

    private void handleIsRegisteredError(Throwable throwable, SingleLiveEvent<Boolean> observer) {
        observer.setValue(false);
        throwable.printStackTrace();
        GuiUtils.showMessage(throwable.getLocalizedMessage());
    }

    public SingleLiveEvent<Boolean> signOut() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

        subscribeInIOThread(authManager.signOut(),
                new SingleData<>(
                        observer::setValue,
                        throwable -> handleError(observer, throwable)));

        return observer;
    }

    private void handleError(SingleLiveEvent<Boolean> observer, Throwable throwable) {
        observer.setValue(false);
        throwable.printStackTrace();
        GuiUtils.showMessage(throwable.getLocalizedMessage());
    }
}
