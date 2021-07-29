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

    public Single<String> handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        return Single.create(emitter -> {
            AuthCredential credential = getCredential(completedTask);
            String email = getEmail(completedTask);
            signInWithFirebase(credential, email, emitter);
        });
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

    private AuthCredential getCredential(Task<GoogleSignInAccount> completedTask) {
        String idToken = completedTask.getResult().getIdToken();

        return GoogleAuthProvider.getCredential(idToken, null);
    }

    private String getEmail(Task<GoogleSignInAccount> completedTask) {
        return completedTask.getResult().getEmail();
    }

    private void updateUserEmail(String email) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.updateEmail(email);
        }
    }

    public Intent getSignInIntent() {
        if (signInInProcess) {
            return null;
        }
        signInInProcess = true;

        GoogleSignInOptions signInOptions = getSignInOptions();
        GoogleSignInClient signInClient = GoogleSignIn.getClient(FFApp.get(), signInOptions);

        return signInClient.getSignInIntent();
    }

    private GoogleSignInOptions getSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(ObfuscateData.getWebClientId())
                .requestEmail()
                .build();
    }
}
