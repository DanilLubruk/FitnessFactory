package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.firestoreCollections.CoachAccessCollection;
import com.example.fitnessfactory.data.models.CoachAccessEntry;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;

import io.reactivex.Single;

public class CoachesAccessRepository extends BaseRepository {

    @Override
    protected String getRoot() {
        return CoachAccessCollection.getRoot();
    }

    public Single<WriteBatch> getDeleteCoachAccessEntryBatchAsync(String ownerId, String email) {
        return Single.create(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getDeleteCoachAccessEntryBatch(ownerId, email));
            }
        });
    }

    private WriteBatch getDeleteCoachAccessEntryBatch(String ownerId, String email) throws Exception {
        return getFirestore().batch().delete(getDocReference(ownerId, email));
    }

    private DocumentReference getDocReference(String ownerId, String email) throws Exception {
        List<DocumentSnapshot> documentSnapshots =
                Tasks.await(
                getCollection()
                        .whereEqualTo(CoachAccessEntry.OWNER_ID_FIELD, ownerId)
                        .whereEqualTo(CoachAccessEntry.USER_EMAIL_FIELD, email).get()).getDocuments();

        checkDataEmpty(documentSnapshots);
        checkEmailUniqueness(documentSnapshots);

        return documentSnapshots.get(0).getReference();
    }
}
