package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.firestoreCollections.OwnerCoachesCollection;
import com.example.fitnessfactory.data.models.Coach;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public class OwnerCoachesRepository extends BaseRepository {

    @Override
    protected String getRoot() {
        return OwnerCoachesCollection.getRoot();
    }

    public Single<WriteBatch> getDeleteCoachBatchAsync(WriteBatch writeBatch, String email) {
        return Single.create(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getDeleteCoachBatch(writeBatch, email));
            }
        });
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

    public Single<List<String>> getCoachesEmailsAsync() {
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
}
