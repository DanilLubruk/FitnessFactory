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

    public Intent getSignInIntent() {
        return authManager.getSignInIntent();
    }

    public SingleLiveEvent<List<AppUser>> handleSignIn(Task<GoogleSignInAccount> completedTask) {
        SingleLiveEvent<List<AppUser>> observer = new SingleLiveEvent<>();

        AtomicReference<String> userEmail = new AtomicReference<>();
        addSubscription(authManager.handleSignInResult(completedTask)
                .observeOn(getIOScheduler())
                .subscribeOn(getIOScheduler())
                .flatMap(email -> {
                    userEmail.set(email);
                    return userRepository.isRegistered(email);
                })
                .flatMap(isRegistered ->
                        isRegistered ?
                                Single.just(userEmail.get()) :
                                userRepository.registerUser(
                                        userEmail.get(),
                                        FirebaseAuthManager.getCurrentUserName()))
                .flatMap(email -> accessRepository.getOwnersByInvitedEmail(email))
                .flatMap(ownersIds -> userRepository.getOwnersByIds(ownersIds))
                .observeOn(getMainThreadScheduler())
                .subscribe(observer::setValue, throwable -> handleAuthError(throwable, observer)));

        return observer;
    }

    /*public SingleLiveEvent<String> handleSignIn(Task<GoogleSignInAccount> completedTask) {
        SingleLiveEvent<String> observer = new SingleLiveEvent<>();

        subscribeInIOThread(authManager.handleSignInResult(completedTask),
                new SingleData<>(
                        observer::setValue,
                        throwable -> handleAuthError(throwable, observer)));

        return observer;
    }*/

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
}
