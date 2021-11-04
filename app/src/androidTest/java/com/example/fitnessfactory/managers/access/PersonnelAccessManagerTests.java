package com.example.fitnessfactory.managers.access;

import com.example.fitnessfactory.BaseTests;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.repositories.access.PersonnelAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.data.repositories.ownerData.participantsData.ParticipantSessionsRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public abstract class PersonnelAccessManagerTests extends BaseTests {

    String ownerId = "userId2";
    String personnelEmail = "useremail1";

    protected abstract PersonnelAccessManager getPersonnelAccessManager();

    protected abstract PersonnelAccessRepository getAccessRepository();

    protected abstract OwnerPersonnelRepository getOwnersRepository();

    @Test
    public void tryRegisterRegisteredPersonnelTest() {
        TestObserver<Boolean> subscriber =
                subscribeInTestThread(getPersonnelAccessManager().createPersonnel(ownerId, personnelEmail));

        subscriber.assertError(Exception.class);
        subscriber.assertNotComplete();
        String errorMessage = subscriber.errors().get(0).getLocalizedMessage();
        assertEquals(ResUtils.getString(R.string.message_admin_is_registered), errorMessage);
    }

    @Test
    public void tryRegisterAddedPersonnelTest() {
        Mockito.when(getOwnersRepository().isPersonnelWithThisEmailAddedAsync(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String email = invocation.getArgument(0);

                    if (email.equals("useremail5")) {
                        return Single.just(true);
                    } else {
                        return Single.just(false);
                    }
                });

        TestObserver<Boolean> subscriber =
                subscribeInTestThread(getPersonnelAccessManager().createPersonnel(ownerId, "useremail5"));

        Mockito.verify(getAccessRepository())
                .getRegisterPersonnelAccessEntryBatchAsync(ownerId, "useremail5");

        Mockito.verify(getOwnersRepository(), Mockito.times(0))
                .getAddPersonnelBatchAsync(Mockito.any(), Mockito.any());

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        boolean isRegistered = subscriber.values().get(0);
        assertTrue(isRegistered);
        subscriber.dispose();
    }

    @Test
    public void registerPersonnelTest() {
        TestObserver<Boolean> subscriber =
                subscribeInTestThread(getPersonnelAccessManager().createPersonnel(ownerId, "useremail6"));

        Mockito.verify(getAccessRepository())
                .getRegisterPersonnelAccessEntryBatchAsync(ownerId, "useremail6");

        Mockito.verify(getOwnersRepository()).getAddPersonnelBatchAsync(Mockito.any(), Mockito.eq("useremail6"));

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
                subscribeInTestThread(getPersonnelAccessManager().deletePersonnelSingle(ownerId, personnelEmail));

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
                subscribeInTestThread(getPersonnelAccessManager().deletePersonnelSingle("ownerId", personnelEmail));

        subscriber.assertError(Exception.class);
        subscriber.assertNotComplete();
        subscriber.dispose();
    }

    private void setDeletePersonnelMocks() {
        WriteBatch writeBatch = FirebaseFirestore.getInstance().batch();

        Mockito.when(getAccessRepository()
                .getDeletePersonnelAccessEntryBatchAsync(Mockito.anyString(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String id = invocation.getArgument(0);
                    String email = invocation.getArgument(1);

                    if (id.equals(ownerId) && email.equals(personnelEmail)) {
                        return Single.just(writeBatch);
                    } else {
                        return Single.error(new Exception());
                    }
                });

        Mockito.when(getOwnersRepository()
                .getDeletePersonnelBatchAsync(Mockito.any(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    WriteBatch argumentWriteBatch = invocation.getArgument(0);
                    String email = invocation.getArgument(1);

                    if (argumentWriteBatch.equals(writeBatch) && email.equals(personnelEmail)) {
                        return Single.just(writeBatch);
                    } else {
                        return Single.error(new Exception());
                    }
                });
    }
}
