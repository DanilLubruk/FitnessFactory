package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.data.firestoreCollections.OwnerCoachesCollection;
import com.example.fitnessfactory.data.models.Coach;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public class OwnerCoachesRepository extends BaseRepository implements OwnerPersonnelRepository {

    @Override
    protected String getRoot() {
        return OwnerCoachesCollection.getRoot();
    }

    @Override
    public Single<Boolean> isPersonnelWithThisEmailAdded(String email) {
        return Single.create(emitter -> {
            boolean isCoachAdded = isCoachWithThisEmailAdded(email);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isCoachAdded);
            }
        });
    }

    private boolean isCoachWithThisEmailAdded(String email) throws ExecutionException, InterruptedException {
        QuerySnapshot querySnapshot = Tasks.await(getCollection().whereEqualTo(Coach.USER_EMAIL_FIELD, email).get());

        return querySnapshot.getDocuments().size() > 0;
    }

    @Override
    public Single<WriteBatch> getAddPersonnelBatch(WriteBatch writeBatch, String email) {
        return Single.create(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getAddCoachBatch(writeBatch, email));
            }
        });
    }

    private WriteBatch getAddCoachBatch(WriteBatch writeBatch, String email) {
        DocumentReference document = getCollection().document();
        Coach coach = new Coach();
        coach.setUserEmail(email);

        return writeBatch.set(document, coach);
    }

    @Override
    public Single<WriteBatch> getDeletePersonnelBatch(WriteBatch writeBatch, String email) {
        return Single.create(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getDeleteCoachBatch(writeBatch, email));
            }
        });
    }

    @Override
    public Single<List<String>> getPersonnelEmails() {
        return Single.create(emitter -> {
            List<String> coachesEmails = getCoachesEmails();

            if (!emitter.isDisposed()) {
                emitter.onSuccess(coachesEmails);
            }
        });
    }

    private List<String> getCoachesEmails() throws ExecutionException, InterruptedException {
        List<Coach> coaches = Tasks.await(getCollection().get()).toObjects(Coach.class);

        List<String> coachesEmails = new ArrayList<>();
        for (Coach coach : coaches) {
            coachesEmails.add(coach.getUserEmail());
        }

        return coachesEmails;
    }

    @Override
    public Single<List<String>> getPersonnelEmailsByGymId(String gymId) {
        return Single.create(emitter -> {
            List<String> coachesEmails = getCoachesEmailsByGymId(gymId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(coachesEmails);
            }
        });
    }

    private List<String> getCoachesEmailsByGymId(String gymId) throws ExecutionException, InterruptedException {
        List<Coach> coaches =
                Tasks.await(
                        getCollection()
                        .whereArrayContains(Coach.GYMS_ARRAY_FIELD, gymId)
                                .get())
                        .toObjects(Coach.class);

        List<String> coachesEmails = new ArrayList<>();
        for (Coach coach : coaches) {
            coachesEmails.add(coach.getUserEmail());
        }

        return coachesEmails;
    }

    private WriteBatch getDeleteCoachBatch(WriteBatch writeBatch, String email) throws Exception {
        return writeBatch.delete(getCoachDocReference(email));
    }

    private DocumentReference getCoachDocReference(String email) throws Exception {
        List<DocumentSnapshot> documentSnapshots =
                Tasks.await(
                        getCollection().whereEqualTo(Coach.USER_EMAIL_FIELD, email)
                                .get())
                        .getDocuments();

        checkDataEmpty(documentSnapshots);
        checkEmailUniqueness(documentSnapshots);

        return documentSnapshots.get(0).getReference();
    }


}
