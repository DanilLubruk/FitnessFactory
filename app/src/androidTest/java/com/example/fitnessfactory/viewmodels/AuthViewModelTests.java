package com.example.fitnessfactory.viewmodels;

import android.content.Intent;

import com.example.fitnessfactory.BaseTests;
import com.example.fitnessfactory.TestRxUtils;
import com.example.fitnessfactory.data.managers.AuthManager;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.OrganisationInfoRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.mockHelpers.mockers.AuthManagerMocker;
import com.example.fitnessfactory.mockHelpers.mockers.FirebaseAuthManagerMocker;
import com.example.fitnessfactory.system.FirebaseAuthManager;
import com.example.fitnessfactory.ui.viewmodels.AuthViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import io.reactivex.Single;

public class AuthViewModelTests extends BaseTests {

    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private AdminsAccessRepository adminsAccessRepository = Mockito.mock(AdminsAccessRepository.class);

    private FirebaseAuthManager firebaseAuthManager = Mockito.mock(FirebaseAuthManager.class);

    private OrganisationInfoRepository organisationInfoRepository = Mockito.mock(OrganisationInfoRepository.class);

    private AuthManager authManager;

    private AuthViewModel authViewModel;

    @Before
    public void setup() {
        super.setup();

        authManager = AuthManagerMocker.createMocker(
                userRepository,
                adminsAccessRepository,
                firebaseAuthManager,
                organisationInfoRepository);

        authViewModel = new AuthViewModel(authManager, firebaseAuthManager);
        authViewModel.setIOScheduler(testScheduler);
        authViewModel.setMainThreadScheduler(testScheduler);
        authViewModel.setRxErrorsHandler(new TestRxUtils());
    }

    @Test
    public void signInRegisteredUserTest() {
        Intent signInData = new Intent();
        signInData.putExtra(FirebaseAuthManagerMocker.NAME_EXTRA, "User1");
        signInData.putExtra(FirebaseAuthManagerMocker.EMAIL_EXTRA, "useremail1");

        Mockito.when(firebaseAuthManager.getSignInIntentAsync())
                .thenReturn(Single.just(signInData));

        SingleLiveEvent<Intent> intentResult = authViewModel.getSignInIntent();
        testScheduler.triggerActions();
        Intent receivedIntent = getOrAwaitValue(intentResult);
        assertNotNull(receivedIntent);
        assertEquals("User1", receivedIntent.getStringExtra(FirebaseAuthManagerMocker.NAME_EXTRA));
        assertEquals("useremail1", receivedIntent.getStringExtra(FirebaseAuthManagerMocker.EMAIL_EXTRA));


    }
}
