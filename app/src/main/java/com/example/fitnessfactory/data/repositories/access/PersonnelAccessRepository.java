package com.example.fitnessfactory.data.repositories.access;

import com.example.fitnessfactory.data.models.PersonnelAccessEntry;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public abstract class PersonnelAccessRepository extends BaseRepository {

    public Single<Boolean> isPersonnelWithThisEmailRegisteredAsync(String ownerId, String userEmail) {
        return SingleCreate(emitter -> {
            boolean isPersonnelRegistered = isPersonnelWithThisEmailRegistered(ownerId, userEmail);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isPersonnelRegistered);
            }
        });
    }

    private boolean isPersonnelWithThisEmailRegistered(String ownerId, String userEmail) throws ExecutionException, InterruptedException {
        int personnelAmount =
                getEntitiesAmount(
                        newQuery()
                                .whereOwnerIdEquals(ownerId)
                                .whereUserEmailEquals(userEmail)
                                .build());

        return personnelAmount > 0;
    }

    public Single<WriteBatch> getRegisterPersonnelAccessEntryBatchAsync(String ownerId, String userEmail) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getRegisterPersonnelAccessEntryBatch(ownerId, userEmail));
            }
        });
    }

    private WriteBatch getRegisterPersonnelAccessEntryBatch(String ownerId, String userEmail) {
        DocumentReference document = getCollection().document();
        PersonnelAccessEntry personnelAccessEntry = new PersonnelAccessEntry();
        personnelAccessEntry.setOwnerId(ownerId);
        personnelAccessEntry.setUserEmail(userEmail);

        return getFirestore().batch().set(document, personnelAccessEntry);
    }

    public Single<WriteBatch> getDeletePersonnelAccessEntryBatchAsync(String ownerId, String email) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getDeletePersonnelAccessEntryBatch(ownerId, email));
            }
        });
    }

    private WriteBatch getDeletePersonnelAccessEntryBatch(String ownerId, String email) throws Exception {
        return getFirestore().batch().delete(getPersonnelDocument(ownerId, email));
    }

    private DocumentReference getPersonnelDocument(String ownerId, String email) throws Exception {
        return getPersonnelSnapshot(ownerId, email).getReference();
    }

    private DocumentSnapshot getPersonnelSnapshot(String ownerId, String email) throws Exception {
        return getUniqueUserEntitySnapshot(
                newQuery()
                        .whereOwnerIdEquals(ownerId)
                        .whereUserEmailEquals(email)
                        .build());
    }

    protected abstract PersonnelAccessRepository.QueryBuilder newQuery();

    protected class QueryBuilder {

        Query query = getCollection();

        public QueryBuilder whereEmailInArray(List<String> personnelEmails) {
            query = query.whereIn(PersonnelAccessEntry.USER_EMAIL_FIELD, personnelEmails);
            return this;
        }

        public QueryBuilder whereUserEmailEquals(String userEmail) {
            query = query.whereEqualTo(PersonnelAccessEntry.USER_EMAIL_FIELD, userEmail);
            return this;
        }

        public QueryBuilder whereOwnerIdEquals(String ownerId) {
            query = query.whereEqualTo(PersonnelAccessEntry.OWNER_ID_FIELD, ownerId);
            return this;
        }

        public Query build() {
            return query;
        }
    }
}
