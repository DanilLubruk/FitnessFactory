package com.example.fitnessfactory.system;

import android.content.Intent;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.security.ObfuscateData;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public class FirebaseAuthManager {

    private final FirebaseAuth mAuth;
    private boolean signInInProcess = false;
    private final UserRepository userRepository;

    @Inject
    public FirebaseAuthManager(UserRepository userRepository) {
        this.userRepository = userRepository;
        mAuth = FirebaseAuth.getInstance();
    }

    public Single<Boolean> isCurrentUserOwnerAsync() {
        return getCurrentUserEmail()
                .flatMap(userEmail -> userRepository.getAppUserByEmailAsync(userEmail))
                .flatMap(user -> Single.just(user.getId().equals(AppPrefs.gymOwnerId().getValue())));
    }

    private Single<String> getCurrentUserEmail() {
        return Single.create(emitter -> {
            if (mAuth == null || mAuth.getCurrentUser() == null) {
                if (!emitter.isDisposed()) {
                    emitter.onError(new Exception(ResUtils.getString(R.string.caption_user_null)));
                }
                return;
            }

            if (!emitter.isDisposed()) {
                emitter.onSuccess(mAuth.getCurrentUser().getEmail());
            }
        });
    }

    public Single<Boolean> isLoggedIn() {
        return Single.create(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(mAuth.getCurrentUser() != null);
            }
        });
    }

    public Single<AppUser> handleSignInResult(Intent data) {
        return Single.create(emitter -> {
            GoogleSignInAccount signInAccount = Tasks.await(GoogleSignIn.getSignedInAccountFromIntent(data));
            AuthCredential credential = getCredential(signInAccount);
            String email = signInAccount.getEmail();
            signInWithFirebase(credential, email, emitter);
        });
    }

    private AuthCredential getCredential(GoogleSignInAccount signInAccount) {
        String idToken = signInAccount.getIdToken();

        return GoogleAuthProvider.getCredential(idToken, null);
    }

    private void signInWithFirebase(AuthCredential credential,
                                    String email,
                                    SingleEmitter<AppUser> emitter){
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener((task) -> {
                    FirebaseUser authUser = task.getUser();
                    if (authUser == null) {
                        if (!emitter.isDisposed()) {
                            emitter.onError(new Exception(getNullUserErrorMessage()));
                        }
                        return;
                    }

                    if (!emitter.isDisposed()) {
                        signInInProcess = false;
                        authUser.updateEmail(email);
                        AppUser currentUser = new AppUser();
                        currentUser.setName(authUser.getDisplayName());
                        currentUser.setEmail(authUser.getEmail());
                        emitter.onSuccess(currentUser);
                    }

                })
                .addOnFailureListener(exception -> {

                    if (!emitter.isDisposed()) {
                        signInInProcess = false;
                        emitter.onError(exception);
                    }

                });
    }

    private String getNullUserErrorMessage() {
        return ResUtils.getString(R.string.caption_wrong_auth)
                .concat(" : ")
                .concat(ResUtils.getString(R.string.caption_user_null));
    }

    private void updateUserEmail(String email) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.updateEmail(email);
        }
    }

    public Single<Intent> getSignInIntentAsync() {
        return Single.create(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getSignInIntent());
            }
        });
    }

    private Intent getSignInIntent() throws Exception {
        if (signInInProcess) {
            throw new Exception(ResUtils.getString(R.string.message_error_sign_in_process));
        }
        signInInProcess = true;

        return getGoogleSignInClient().getSignInIntent();
    }

    public Completable interruptSignInAsync() {
        return Completable.create(source -> {
            interruptSignIn();

            if (!source.isDisposed()) {
                source.onComplete();
            }
        });
    }

    private void interruptSignIn() {
        signInInProcess = false;
    }

    private GoogleSignInClient getGoogleSignInClient() {
        return GoogleSignIn.getClient(FFApp.get(), getSignInOptions());
    }

    private GoogleSignInOptions getSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(ObfuscateData.getWebClientId())
                .requestEmail()
                .build();
    }

    public Completable signOutCompletable() {
        return Completable.create(emitter -> {
            try {
                signOut();
            } catch (InterruptedException e) {
                emitter.onError(e);
            } catch (Exception e) {
                emitter.onError(e);
            }

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    public Single<Boolean> signOutSingle() {
        return Single.create(emitter -> {
            boolean isSignedOut = false;
            try {
                isSignedOut = signOut();
            } catch (InterruptedException e) {
                emitter.onError(e);
            } catch (Exception e) {
                emitter.onError(e);
            }

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isSignedOut);
            }
        });
    }

    private boolean signOut() throws ExecutionException, InterruptedException {
        mAuth.signOut();

        boolean isFirebaseAuthSignedOut = mAuth.getCurrentUser() == null;
        boolean isGoogleSignedOut = signOutGoogle();

        return isFirebaseAuthSignedOut && isGoogleSignedOut;
    }

    private boolean signOutGoogle() throws ExecutionException, InterruptedException {
        Tasks.await(getGoogleSignInClient().signOut());

        return true;
    }
}
