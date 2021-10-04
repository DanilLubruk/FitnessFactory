package com.example.fitnessfactory.mockHelpers.mockers;

import android.content.Intent;

import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.system.FirebaseAuthManager;

import org.mockito.Mockito;

import io.reactivex.Single;

public class FirebaseAuthManagerMocker {

    public static final String NAME_EXTRA = "NAME_EXTRA";
    public static final String EMAIL_EXTRA = "EMAIL_EXTRA";

    public static FirebaseAuthManager createMocker(FirebaseAuthManager firebaseAuthManager) {
        Mockito.when(firebaseAuthManager.handleSignInResult(Mockito.any()))
                .thenAnswer(invocation -> {
                    Intent data = invocation.getArgument(0);

                    String userName = data.getStringExtra(NAME_EXTRA);
                    String userEmail = data.getStringExtra(EMAIL_EXTRA);

                    AppUser appUser = AppUser
                            .builder()
                            .setName(userName)
                            .setEmail(userEmail)
                            .build();

                    return Single.just(appUser);
                });

        return firebaseAuthManager;
    }
}
