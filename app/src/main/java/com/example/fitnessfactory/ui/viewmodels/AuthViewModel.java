package com.example.fitnessfactory.ui.viewmodels;

import android.content.Intent;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.CurrentUserType;
import com.example.fitnessfactory.data.managers.AuthManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleDialogEvent;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.ownerData.OwnersRepository;
import com.example.fitnessfactory.system.FirebaseAuthManager;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class AuthViewModel extends BaseViewModel {

    private AuthManager authManager;
    private FirebaseAuthManager firebaseAuthManager;
    private OwnersRepository ownersRepository;

    @Inject
    public AuthViewModel(AuthManager authManager,
                         FirebaseAuthManager firebaseAuthManager,
                         OwnersRepository ownersRepository) {
        this.authManager = authManager;
        this.firebaseAuthManager = firebaseAuthManager;
        this.ownersRepository = ownersRepository;
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
        subscribeInIOThread(firebaseAuthManager.interruptSignInAsync());
    }

    public SingleLiveEvent<Boolean> signInUser(Intent authData,
                                               SingleDialogEvent<AppUser, List<AppUser>> dialogEvent) {
        SingleLiveEvent<Boolean> isSignedIn = new SingleLiveEvent<>();

        addSubscription(authManager.handleSignIn(authData)
                .flatMap(owners -> {
                    if (owners != null) {
                        return dialogEvent.showDialog(owners);
                    } else {
                        return Single.error(new Exception("Sign in failed"));
                    }
                })
                .subscribeOn(getIOScheduler())
                .observeOn(getIOScheduler())
                .flatMap(user -> {
                    if (CurrentUserType.isOwner()) {
                        return ownersRepository.createOwnerAsync(user);
                    } else {
                        return Single.just(user);
                    }
                })
                .subscribeOn(getIOScheduler())
                .observeOn(getIOScheduler())
                .flatMapCompletable(value -> authManager.checkOrganisationName())
                .subscribeOn(getMainThreadScheduler())
                .observeOn(getMainThreadScheduler())
                .subscribe(
                        () -> isSignedIn.setValue(true),
                        throwable -> getErrorHandler().handleError(isSignedIn, throwable)));

        return isSignedIn;
    }

    public void signOut() {
        subscribeInIOThread(firebaseAuthManager.signOutCompletable());
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
