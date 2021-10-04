package com.example.fitnessfactory.ui.viewmodels;

import android.content.Intent;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.managers.AuthManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.system.FirebaseAuthManager;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.RxUtils;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class AuthViewModel extends BaseViewModel {

    @Inject
    AuthManager authManager;
    @Inject
    FirebaseAuthManager firebaseAuthManager;

    public AuthViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public SingleLiveEvent<Boolean> isLoggedIn() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

        subscribeInIOThread(
                firebaseAuthManager.isLoggedIn(),
                new SingleData<>(observer::setValue, getErrorHandler()::handleError));

        return observer;
    }

    public SingleLiveEvent<Intent> getSignInIntent() {
        SingleLiveEvent<Intent> observer = new SingleLiveEvent<>();

        subscribeInIOThread(firebaseAuthManager.getSignInIntentAsync(),
                new SingleData<>(
                        observer::setValue,
                        getErrorHandler()::handleError));

        return observer;
    }

    public void interruptSignIn() {
        subscribeInIOThread(firebaseAuthManager.interruptSignInAsync(),
                getErrorHandler()::handleError);
    }

    public SingleLiveEvent<List<AppUser>> handleSignIn(Intent authData) {
        SingleLiveEvent<List<AppUser>> observer = new SingleLiveEvent<>();

        subscribe(authManager.handleSignIn(authData),
                new SingleData<>(
                        observer::setValue,
                        throwable -> handleAuthError(throwable, observer)));

        return observer;
    }

    public void signOut() {
        subscribeInIOThread(firebaseAuthManager.signOutCompletable(), getErrorHandler()::handleError);
    }

    public SingleLiveEvent<Boolean> checkOrganisationName() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

        subscribe(authManager.checkOrganisationName(),
                () -> observer.setValue(true),
                throwable -> getErrorHandler().handleError(observer, throwable));

        return observer;
    }

    private void handleAuthError(Throwable throwable, SingleLiveEvent<List<AppUser>> observer) {
        observer.setValue(null);
        throwable.printStackTrace();
        String message = ResUtils.getString(R.string.caption_wrong_auth)
                .concat(" - ")
                .concat(throwable.getLocalizedMessage() != null ? throwable.getLocalizedMessage() : "");
        GuiUtils.showMessage(message);
    }
}
