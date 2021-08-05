package com.example.fitnessfactory.system;
import android.content.Intent;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.security.ObfuscateData;
import com.example.fitnessfactory.utils.ResUtils;
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

    public Single<String> handleSignInResult(Intent data) {
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
