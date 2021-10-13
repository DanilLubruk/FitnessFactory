package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.data.firestoreCollections.OwnerCoachesCollection;
import com.example.fitnessfactory.data.models.Coach;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.Single;

public class OwnerCoachesRepository extends BaseRepository implements OwnerPersonnelRepository {

    @Override
    protected String getRoot() {
        return OwnerCoachesCollection.getRoot();
    }

    @Override
    public Single<Boolean> isPersonnelWithThisEmailAdded(String email) {
        return SingleCreate(emitter -> {
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
        return SingleCreate(emitter -> {
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
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getDeleteCoachBatch(writeBatch, email));
            }
        });
    }

    private WriteBatch getDeleteCoachBatch(WriteBatch writeBatch, String email) throws Exception {
        return writeBatch.delete(getCoachDocReference(email));
    }

    @Override
    public Single<List<String>> getPersonnelEmails() {
        return SingleCreate(emitter -> {
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
        return SingleCreate(emitter -> {
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

    @Override
    public Completable addGymToPersonnel(String coachEmail, String gymId) {
        return CompletableCreate(emitter -> {
            addGymToCoach(coachEmail, gymId);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private void addGymToCoach(String coachEmail, String gymId) throws Exception {
        Tasks.await(getCoachDocReference(coachEmail).update(Coach.GYMS_ARRAY_FIELD, FieldValue.arrayUnion(gymId)));
    }

    @Override
    public Completable removeGymFromPersonnel(String coachEmail, String gymId) {
        return CompletableCreate(emitter -> {
            removeGymFromCoach(coachEmail, gymId);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private void removeGymFromCoach(String coachEmail, String gymId) throws Exception {
        Tasks.await(getCoachDocReference(coachEmail).update(Coach.GYMS_ARRAY_FIELD, FieldValue.arrayRemove(gymId)));
    }

    @Override
    public Single<List<String>> getPersonnelGymsIdsByEmail(String personnelEmail) {
        return SingleCreate(emitter -> {
            List<String> coachGymsIds = getCoachGymsByEmail(personnelEmail);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(coachGymsIds);
            }
        });
    }

    private List<String> getCoachGymsByEmail(String coachEmail) throws Exception {
        List<String> coachGymsIds = new ArrayList<>();

        List<Coach> coaches = Tasks.await(
                getCollection().whereEqualTo(Coach.USER_EMAIL_FIELD, coachEmail).get()).toObjects(Coach.class);
        if (coaches.isEmpty()) {
            return coachGymsIds;
        }

        checkEmailUniqueness(coaches);

        Coach coach = coaches.get(0);
        if (coach.getGymsIds() != null) {
            coachGymsIds = coach.getGymsIds();
        }

        return coachGymsIds;
    }

    private DocumentReference getCoachDocReference(String email) throws Exception {
        return getUniqueUserEntityReference(getCollection().whereEqualTo(Coach.USER_EMAIL_FIELD, email));
    }

    public Single<WriteBatch> getRemoveGymFromCoachBatchAsync(WriteBatch writeBatch, String gymId) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getRemoveGymFromCoachBatch(writeBatch, gymId));
            }
        });
    }

    private WriteBatch getRemoveGymFromCoachBatch(WriteBatch writeBatch, String gymId)
            throws ExecutionException, InterruptedException {
        for (DocumentSnapshot documentSnapshot : getCoachesListSnapshotsByGymId(gymId)) {
            writeBatch = writeBatch
                    .update(
                            documentSnapshot.getReference(),
                            Coach.GYMS_ARRAY_FIELD,
                            FieldValue.arrayRemove(gymId));
        }

        return writeBatch;
    }

    private List<DocumentSnapshot> getCoachesListSnapshotsByGymId(String gymId)
            throws ExecutionException, InterruptedException {
        return Tasks.await(getCollection().whereArrayContains(Coach.GYMS_ARRAY_FIELD, gymId).get()).getDocuments();
    }
}
