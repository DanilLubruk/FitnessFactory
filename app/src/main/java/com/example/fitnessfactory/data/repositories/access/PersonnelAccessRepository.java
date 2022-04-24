package com.example.fitnessfactory.data.repositories.access;

import com.example.fitnessfactory.data.models.PersonnelAccessEntry;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public abstract class PersonnelAccessRepository extends BaseRepository {

    public Single<Boolean> isPersonnelWithThisIdRegisteredAsync(String ownerId, String userId) {
        return SingleCreate(emitter -> {
            boolean isPersonnelRegistered = isPersonnelWithThisIdRegistered(ownerId, userId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isPersonnelRegistered);
            }
        });
    }

    private boolean isPersonnelWithThisIdRegistered(String ownerId, String userId) throws ExecutionException, InterruptedException {
        int personnelAmount =
                getEntitiesAmount(
                        newQuery()
                                .whereOwnerIdEquals(ownerId)
                                .whereUserIdEquals(userId)
                                .build());

        return personnelAmount > 0;
    }

    public Single<WriteBatch> getRegisterPersonnelAccessEntryBatchAsync(String ownerId, String userId) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getRegisterPersonnelAccessEntryBatch(ownerId, userId));
            }
        });
    }

    private WriteBatch getRegisterPersonnelAccessEntryBatch(String ownerId, String userId) {
        DocumentReference document = getCollection().document(userId);
        PersonnelAccessEntry personnelAccessEntry = new PersonnelAccessEntry();
        personnelAccessEntry.setOwnerId(ownerId);
        personnelAccessEntry.setUserId(userId);

        return getFirestore().batch().set(document, personnelAccessEntry);
    }

    public Single<WriteBatch> getDeletePersonnelAccessEntryBatchAsync(String ownerId, String userId) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getDeletePersonnelAccessEntryBatch(ownerId, userId));
            }
        });
    }

    private WriteBatch getDeletePersonnelAccessEntryBatch(String ownerId, String userId) throws Exception {
        return getFirestore().batch().delete(getPersonnelDocument(ownerId, userId));
    }

    private DocumentReference getPersonnelDocument(String ownerId, String userId) throws Exception {
        return getPersonnelSnapshot(ownerId, userId).getReference();
    }

    private DocumentSnapshot getPersonnelSnapshot(String ownerId, String userId) throws Exception {
        return getUniqueUserEntitySnapshot(
                newQuery()
                        .whereOwnerIdEquals(ownerId)
                        .whereUserIdEquals(userId)
                        .build());
    }

    protected abstract PersonnelAccessRepository.QueryBuilder newQuery();

    protected class QueryBuilder {

        Query query = getCollection();

        public QueryBuilder whereEmailInArray(List<String> personnelEmails) {
            query = query.whereIn(PersonnelAccessEntry.USER_ID_FIELD, personnelEmails);
            return this;
        }

        public QueryBuilder whereUserIdEquals(String userId) {
            query = query.whereEqualTo(PersonnelAccessEntry.USER_ID_FIELD, userId);
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
