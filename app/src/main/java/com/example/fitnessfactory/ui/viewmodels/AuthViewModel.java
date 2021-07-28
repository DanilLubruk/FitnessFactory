package com.example.fitnessfactory.ui.viewmodels;

import android.app.Activity;
import android.content.Intent;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.system.FirebaseAuthManager;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import io.reactivex.Single;

public class AuthViewModel extends BaseViewModel {

    @Inject
    UserRepository userRepository;
    @Inject
    FirebaseAuthManager authManager;


    public AuthViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public Intent getSignInIntent() {
        return authManager.getSignInIntent();
    }

    public SingleLiveEvent<Boolean> handleSignIn(Task<GoogleSignInAccount> completedTask) {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

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
                                Single.just(true) :
                                userRepository.registerUser(userEmail.get()))
                .observeOn(getMainThreadScheduler())
                .subscribe(observer::setValue, throwable -> handleAuthError(throwable, observer)));

        return observer;
    }

    private void handleAuthError(Throwable throwable, SingleLiveEvent<Boolean> observer) {
        observer.setValue(false);
        throwable.printStackTrace();
        String message = ResUtils.getString(R.string.caption_wrong_auth)
                .concat(" - ")
                .concat(throwable.getLocalizedMessage());
        GuiUtils.showMessage(message);
    }
}
