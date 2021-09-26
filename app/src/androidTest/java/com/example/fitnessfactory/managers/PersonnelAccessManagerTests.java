package com.example.fitnessfactory.managers;

import com.example.fitnessfactory.BaseTests;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.repositories.access.PersonnelAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
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

    protected PersonnelAccessRepository accessRepository;
    protected OwnerPersonnelRepository ownersRepository;

    protected PersonnelAccessManager personnelAccessManager;

    String ownerId = "ownerId1";
    String personnelEmail = "userEmail1";

    @Before
    public void setup() {
        super.setup();
        //TestFFApp.getTestAppComponent().inject(this);
        accessRepository = Mockito.mock(PersonnelAccessRepository.class);
        ownersRepository = Mockito.mock(OwnerPersonnelRepository.class);
        personnelAccessManager = instantiateAccessManager(accessRepository, ownersRepository);
    }

    protected abstract PersonnelAccessManager instantiateAccessManager(PersonnelAccessRepository personnelAccessRepository,
                                                                       OwnerPersonnelRepository ownersRepository);

    @Test
    public void tryRegisterRegisteredPersonnelTest() {
        TestObserver<Boolean> subscriber =
                subscribe(personnelAccessManager.createPersonnel(ownerId, personnelEmail));

        subscriber.assertError(Exception.class);
        subscriber.assertNotComplete();
        String errorMessage = subscriber.errors().get(0).getLocalizedMessage();
        assertEquals(ResUtils.getString(R.string.message_admin_is_registered), errorMessage);
    }

    @Test
    public void tryRegisterAddedPersonnelTest() {
        TestObserver<Boolean> subscriber =
                subscribe(personnelAccessManager.createPersonnel(ownerId, "userEmail5"));

        Mockito.verify(accessRepository)
                .getRegisterPersonnelAccessEntryBatch(ownerId, "userEmail5");

        Mockito.verify(ownersRepository, Mockito.times(0))
                .getAddPersonnelBatch(Mockito.any(), Mockito.any());

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        boolean isRegistered = subscriber.values().get(0);
        assertTrue(isRegistered);
        subscriber.dispose();
    }

    @Test
    public void registerPersonnelTest() {
        TestObserver<Boolean> subscriber =
                subscribe(personnelAccessManager.createPersonnel(ownerId, "userEmail6"));

        Mockito.verify(accessRepository)
                .getRegisterPersonnelAccessEntryBatch(ownerId, "userEmail6");

        Mockito.verify(ownersRepository).getAddPersonnelBatch(Mockito.any(), Mockito.eq("userEmail6"));

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
                subscribe(personnelAccessManager.deletePersonnelSingle(ownerId, personnelEmail));

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
                subscribe(personnelAccessManager.deletePersonnelSingle("ownerId", personnelEmail));

        subscriber.assertError(Exception.class);
        subscriber.assertNotComplete();
        subscriber.dispose();
    }

    private void setDeletePersonnelMocks() {
        WriteBatch writeBatch = FirebaseFirestore.getInstance().batch();

        Mockito.when(accessRepository
                .getDeletePersonnelAccessEntryBatch(Mockito.anyString(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String id = invocation.getArgument(0);
                    String email = invocation.getArgument(1);

                    if (id.equals(ownerId) && email.equals(personnelEmail)) {
                        return Single.just(writeBatch);
                    } else {
                        return Single.error(new Exception());
                    }
                });

        Mockito.when(ownersRepository
                .getDeletePersonnelBatch(Mockito.any(), Mockito.anyString()))
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
