package com.example.fitnessfactory.ui.viewmodels;

import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.system.FirebaseAuthManager;

import javax.inject.Inject;

public class SplashActivityViewModel extends BaseViewModel {

    private FirebaseAuthManager firebaseAuthManager;

    @Inject
    SplashActivityViewModel(FirebaseAuthManager firebaseAuthManager) {
        this.firebaseAuthManager = firebaseAuthManager;
    }

    public SingleLiveEvent<Boolean> isLoggedIn() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

        subscribeInIOThread(
                firebaseAuthManager.isLoggedIn(),
                new SingleData<>(observer::setValue, getErrorHandler()::handleError));

        return observer;
    }
}
