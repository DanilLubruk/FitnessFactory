package com.example.fitnessfactory.managers;

import android.content.Intent;

import com.example.fitnessfactory.BaseTests;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.managers.AuthManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.OrganisationInfoRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.mockHelpers.mockers.AuthManagerMocker;
import com.example.fitnessfactory.mockHelpers.mockers.FirebaseAuthManagerMocker;
import com.example.fitnessfactory.system.FirebaseAuthManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class AuthManagerTests extends BaseTests {

    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private AdminsAccessRepository adminsAccessRepository = Mockito.mock(AdminsAccessRepository.class);

    private FirebaseAuthManager firebaseAuthManager = Mockito.mock(FirebaseAuthManager.class);

    private OrganisationInfoRepository organisationInfoRepository = Mockito.mock(OrganisationInfoRepository.class);

    private AuthManager authManager;

    @Before
    public void setup() {
        super.setup();
        authManager = AuthManagerMocker
                .createMocker(
                        userRepository,
                        adminsAccessRepository,
                        firebaseAuthManager,
                        organisationInfoRepository);
        authManager.setMainThreadScheduler(testScheduler);
        authManager.setIOScheduler(testScheduler);
    }

    @Test
    public void handleRegisteredSignInTest() {
        Intent signInData = new Intent();
        signInData.putExtra(FirebaseAuthManagerMocker.NAME_EXTRA, "User1");
        signInData.putExtra(FirebaseAuthManagerMocker.EMAIL_EXTRA, "useremail1");

        TestObserver<List<AppUser>> subscriber = subscribe(authManager.handleSignIn(signInData));

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        List<AppUser> owners = subscriber.values().get(0);
        subscriber.dispose();

        Mockito.verify(firebaseAuthManager).handleSignInResult(signInData);
        Mockito.verify(userRepository).isUserRegisteredAsync("useremail1");
        Mockito.verify(userRepository).getAppUserByEmailAsync("useremail1");
        Mockito.verify(userRepository, Mockito.times(0))
                .registerUser(Mockito.any(), Mockito.anyString());
        Mockito.verify(adminsAccessRepository).getOwnersByInvitedEmail(Mockito.any());
        Mockito.verify(userRepository).getOwnersByIds(Mockito.anyList(), Mockito.anyString());

        assertEquals(3, owners.size());

        assertEquals("userId1", owners.get(0).getId());
        assertEquals("User1", owners.get(0).getName());
        assertEquals("useremail1", owners.get(0).getEmail());

        assertEquals("userId2", owners.get(1).getId());
        assertEquals("User2", owners.get(1).getName());
        assertEquals("useremail2", owners.get(1).getEmail());

        assertEquals("userId3", owners.get(2).getId());
        assertEquals("User3", owners.get(2).getName());
        assertEquals("useremail3", owners.get(2).getEmail());
    }

    @Test
    public void handleUnregisteredSignInTest() {
        Intent signInData = new Intent();
        signInData.putExtra(FirebaseAuthManagerMocker.NAME_EXTRA, "User6");
        signInData.putExtra(FirebaseAuthManagerMocker.EMAIL_EXTRA, "useremail6");

        TestObserver<List<AppUser>> subscriber = subscribe(authManager.handleSignIn(signInData));

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        List<AppUser> owners = subscriber.values().get(0);
        subscriber.dispose();

        Mockito.verify(firebaseAuthManager).handleSignInResult(signInData);
        Mockito.verify(userRepository).isUserRegisteredAsync("useremail6");
        Mockito.verify(userRepository, Mockito.times(0))
                .getAppUserByEmailAsync(Mockito.any());
        Mockito.verify(userRepository).registerUser("useremail6", "User6");
        Mockito.verify(adminsAccessRepository).getOwnersByInvitedEmail(Mockito.any());
        Mockito.verify(userRepository).getOwnersByIds(Mockito.anyList(), Mockito.anyString());

        assertEquals(2, owners.size());

        assertEquals("newUserId", owners.get(0).getId());
        assertEquals("User6", owners.get(0).getName());
        assertEquals("useremail6", owners.get(0).getEmail());

        assertEquals("userId3", owners.get(1).getId());
        assertEquals("User3", owners.get(1).getName());
        assertEquals("useremail3", owners.get(1).getEmail());
    }

    @Test
    public void checkOrganisationNameTest() {
        AppPrefs.askForOrganisationName().resetToDefaultValue();
        AppPrefs.organisationName().resetToDefaultValue();

        assertFalse(AppPrefs.askForOrganisationName().getValue());
        assertEquals("", AppPrefs.organisationName().getValue());

        TestObserver<Void> subscriber = subscribe(authManager.checkOrganisationName());

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        subscriber.dispose();

        assertTrue(AppPrefs.askForOrganisationName().getValue());

        subscriber = subscribe(organisationInfoRepository.setOrganisationNameAsync("organisationName"));
        subscriber.assertNoErrors();
        subscriber.assertComplete();
        subscriber.dispose();

        subscriber = subscribe(authManager.checkOrganisationName());

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        subscriber.dispose();

        assertTrue(AppPrefs.askForOrganisationName().getValue());
        assertEquals("organisationName", AppPrefs.organisationName().getValue());

        AppPrefs.askForOrganisationName().resetToDefaultValue();
        AppPrefs.organisationName().resetToDefaultValue();
    }
}
