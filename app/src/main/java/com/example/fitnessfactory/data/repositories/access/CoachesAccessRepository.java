package com.example.fitnessfactory.data.repositories.access;

import com.example.fitnessfactory.data.firestoreCollections.CoachAccessCollection;
import com.example.fitnessfactory.data.models.AdminAccessEntry;
import com.example.fitnessfactory.data.models.CoachAccessEntry;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public class CoachesAccessRepository extends BaseRepository implements PersonnelAccessRepository {

    @Override
    protected String getRoot() {
        return CoachAccessCollection.getRoot();
    }

    @Override
    public Single<Boolean> isPersonnelWithThisEmailRegistered(String ownerId, String userEmail) {
        return SingleCreate(emitter -> {
            boolean isCoachRegistered = isCoachWithThisEmailRegistered(ownerId, userEmail);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isCoachRegistered);
            }
        });
    }

    private boolean isCoachWithThisEmailRegistered(String ownerId, String userEmail) throws ExecutionException, InterruptedException {
        QuerySnapshot querySnapshot =
                Tasks.await(
                        getCollection()
                                .whereEqualTo(CoachAccessEntry.OWNER_ID_FIELD, ownerId)
                                .whereEqualTo(CoachAccessEntry.USER_EMAIL_FIELD, userEmail)
                                .get());

        return querySnapshot.getDocuments().size() > 0;
    }

    @Override
    public Single<WriteBatch> getRegisterPersonnelAccessEntryBatch(String ownerId, String userEmail) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getRegisterCoachAccessEntryBatch(ownerId, userEmail));
            }
        });
    }

    private WriteBatch getRegisterCoachAccessEntryBatch(String ownerId, String userEmail) {
        DocumentReference document = getCollection().document();
        CoachAccessEntry coachAccessEntry = new CoachAccessEntry();
        coachAccessEntry.setOwnerId(ownerId);
        coachAccessEntry.setUserEmail(userEmail);

        return getFirestore().batch().set(document, coachAccessEntry);
    }

    @Override
    public Single<WriteBatch> getDeletePersonnelAccessEntryBatch(String ownerId, String email) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getDeleteCoachAccessEntryBatch(ownerId, email));
            }
        });
    }

    private WriteBatch getDeleteCoachAccessEntryBatch(String ownerId, String email) throws Exception {
        return getFirestore().batch().delete(getDocReference(ownerId, email));
    }

    private DocumentReference getDocReference(String ownerId, String email) throws Exception {
        return getUniqueUserEntityReference(
                getCollection()
                .whereEqualTo(CoachAccessEntry.OWNER_ID_FIELD, ownerId)
                .whereEqualTo(CoachAccessEntry.USER_EMAIL_FIELD, email));
    }

    private class QueryBuilder {

        Query query = getCollection();

        public QueryBuilder whereUserEmailEquals(String userEmail) {
            query = query.whereEqualTo(CoachAccessEntry.USER_EMAIL_FIELD, userEmail);
            return this;
        }

        public QueryBuilder whereOwnerIdEquals(String ownerId) {
            query = query.whereEqualTo(CoachAccessEntry.OWNER_ID_FIELD, ownerId);
            return this;
        }

        public Query build() {
            return query;
        }
    }
}
