package com.example.fitnessfactory;

import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;


public class AdminsAccessManagerTests extends BaseTests {

    @Inject
    AdminsAccessRepository adminsAccessRepository;
    @Inject
    OwnerAdminsRepository ownerAdminsRepository;

    AdminsAccessManager adminsAccessManager;

    String ownerId = "testOwner1Id";
    String adminEmail = "myadmin@gmail.com";

    @Before
    public void setup() {
        super.setup();
        TestFFApp.getTestAppComponent().inject(this);
        Mockito.reset(adminsAccessRepository, ownerAdminsRepository);
        adminsAccessManager = new AdminsAccessManager(adminsAccessRepository, ownerAdminsRepository);
    }

    @Test
    public void tryRegisterRegisteredPersonnelTest() {
        Mockito.when(
                adminsAccessRepository
                        .isPersonnelWithThisEmailRegistered(Mockito.anyString(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String id = invocation.getArgument(0);
                    String email = invocation.getArgument(1);

                    if (id.equals(ownerId) && email.equals(adminEmail)) {
                        return Single.just(true);
                    } else {
                        return Single.just(false);
                    }
                });

        TestObserver<Boolean> subscriber =
                subscribe(adminsAccessManager.createPersonnel(ownerId, adminEmail));

        subscriber.assertError(Exception.class);
        subscriber.assertNotComplete();
        String errorMessage = subscriber.errors().get(0).getLocalizedMessage();
        assertEquals(ResUtils.getString(R.string.message_admin_is_registered), errorMessage);
    }

    @Test
    public void tryRegisterAddedPersonnelTest() {
        Mockito.when(
                adminsAccessRepository
                        .isPersonnelWithThisEmailRegistered(Mockito.anyString(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String id = invocation.getArgument(0);
                    String email = invocation.getArgument(1);

                    if (id.equals(ownerId) && email.equals(adminEmail)) {
                        return Single.just(false);
                    } else {
                        return Single.just(true);
                    }
                });

        Mockito.when(adminsAccessRepository.getRegisterPersonnelAccessEntryBatch(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        Mockito.when(ownerAdminsRepository.isPersonnelWithThisEmailAdded(Mockito.anyString()))
                .thenAnswer(invocation -> {
                   String email = invocation.getArgument(0);

                   if (email.equals(adminEmail)) {
                       return Single.just(true);
                   } else {
                       return Single.just(false);
                   }
                });

        TestObserver<Boolean> subscriber =
                subscribe(adminsAccessManager.createPersonnel(ownerId, adminEmail));

        Mockito.verify(adminsAccessRepository)
                .getRegisterPersonnelAccessEntryBatch(ownerId, adminEmail);

        Mockito.verify(ownerAdminsRepository, Mockito.times(0))
                .getAddPersonnelBatch(Mockito.any(), Mockito.any());

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        boolean isRegistered = subscriber.values().get(0);
        assertTrue(isRegistered);
        subscriber.dispose();
    }

    @Test
    public void registerPersonnelTest() {
        Mockito.when(
                adminsAccessRepository
                        .isPersonnelWithThisEmailRegistered(Mockito.anyString(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String id = invocation.getArgument(0);
                    String email = invocation.getArgument(1);

                    if (id.equals(ownerId) && email.equals(adminEmail)) {
                        return Single.just(false);
                    } else {
                        return Single.just(true);
                    }
                });

        Mockito.when(adminsAccessRepository
                .getRegisterPersonnelAccessEntryBatch(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        Mockito.when(ownerAdminsRepository.isPersonnelWithThisEmailAdded(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String email = invocation.getArgument(0);

                    if (email.equals(adminEmail)) {
                        return Single.just(false);
                    } else {
                        return Single.just(true);
                    }
                });

        Mockito.when(ownerAdminsRepository
                .getAddPersonnelBatch(Mockito.any(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        TestObserver<Boolean> subscriber =
                subscribe(adminsAccessManager.createPersonnel(ownerId, adminEmail));

        Mockito.verify(adminsAccessRepository)
                .getRegisterPersonnelAccessEntryBatch(ownerId, adminEmail);

        Mockito.verify(ownerAdminsRepository).getAddPersonnelBatch(Mockito.any(), Mockito.eq(adminEmail));

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        boolean isRegistered = subscriber.values().get(0);
        assertTrue(isRegistered);
        subscriber.dispose();
    }

    @Test
    public void deletePersonnelSingleTest() {
        setDeletePersonnelMocks();

        TestObserver<Boolean> subscriber =
                subscribe(adminsAccessManager.deletePersonnelSingle(ownerId, adminEmail));

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        boolean isDeleted = subscriber.values().get(0);
        assertTrue(isDeleted);
        subscriber.dispose();
    }

    @Test
    public void tryDeletePersonnelStringTest() {
        setDeletePersonnelMocks();

        TestObserver<Boolean> subscriber =
                subscribe(adminsAccessManager.deletePersonnelSingle("ownerId", adminEmail));

        subscriber.assertError(Exception.class);
        subscriber.assertNotComplete();
        subscriber.dispose();
    }

    private void setDeletePersonnelMocks() {
        WriteBatch writeBatch = FirebaseFirestore.getInstance().batch();

        Mockito.when(adminsAccessRepository
                .getDeletePersonnelAccessEntryBatch(Mockito.anyString(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String id = invocation.getArgument(0);
                    String email = invocation.getArgument(1);

                    if (id.equals(ownerId) && email.equals(adminEmail)) {
                        return Single.just(writeBatch);
                    } else {
                        return Single.error(new Exception());
                    }
                });

        Mockito.when(ownerAdminsRepository
                .getDeletePersonnelBatch(Mockito.any(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    WriteBatch argumentWriteBatch = invocation.getArgument(0);
                    String email = invocation.getArgument(1);

                    if (argumentWriteBatch.equals(writeBatch) && email.equals(adminEmail)) {
                        return Single.just(writeBatch);
                    } else {
                        return Single.error(new Exception());
                    }
                });
    }
}
