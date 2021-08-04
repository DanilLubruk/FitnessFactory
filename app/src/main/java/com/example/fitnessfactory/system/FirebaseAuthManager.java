package com.example.fitnessfactory.system;
import android.content.Intent;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.security.ObfuscateData;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public class FirebaseAuthManager {

    private FirebaseAuth mAuth;
    private boolean signInInProcess = false;

    public FirebaseAuthManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static String getCurrentUserName() {
        return isLoggedIn() ? FirebaseAuth.getInstance().getCurrentUser().getDisplayName() : "";
    }

    public Single<String> handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        return Single.create(emitter -> {
            AuthCredential credential = getCredential(completedTask);
            String email = getEmail(completedTask);
            signInWithFirebase(credential, email, emitter);
        });
    }

    private AuthCredential getCredential(Task<GoogleSignInAccount> completedTask) {
        String idToken = completedTask.getResult().getIdToken();

        return GoogleAuthProvider.getCredential(idToken, null);
    }

    private String getEmail(Task<GoogleSignInAccount> completedTask) {
        return completedTask.getResult().getEmail();
    }

    private void signInWithFirebase(AuthCredential credential,
                                    String email,
                                    SingleEmitter<String> emitter) {
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener((task) -> {

                    if (!emitter.isDisposed()) {
                        signInInProcess = false;
                        updateUserEmail(email);
                        emitter.onSuccess(email);
                    }

                })
                .addOnFailureListener(exception -> {

                    if (!emitter.isDisposed()) {
                        signInInProcess = false;
                        emitter.onError(exception);
                    }

                });
    }

    private void updateUserEmail(String email) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.updateEmail(email);
        }
    }

    public Single<Boolean> isUsersIdSaved() {
        return Single.create(emitter -> {
            String userId = AppPrefs.gymOwnerId().getValue();
            boolean isIdSaved = !StringUtils.isEmpty(userId);
            if (!emitter.isDisposed()) {
                emitter.onSuccess(isIdSaved);
            }
        });
    }

    public Intent getSignInIntent() {
        if (signInInProcess) {
            return null;
        }
        signInInProcess = true;

        return getGoogleSignInClient().getSignInIntent();
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

    public Single<Boolean> signOut() {
        return Single.create(emitter -> {
            mAuth.signOut();

            boolean isFirebaseAuthSignedOut = mAuth.getCurrentUser() == null;
            boolean isGoogleSignedOut = signOutGoogle(emitter);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isFirebaseAuthSignedOut && isGoogleSignedOut);
            }
        });
    }

    private boolean signOutGoogle(SingleEmitter<Boolean> emitter) {
        boolean isGoogleSignedOut = false;
        try {
            Tasks.await(getGoogleSignInClient().signOut());
            isGoogleSignedOut = true;
        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
            }
        }

        return isGoogleSignedOut;
    }
}
