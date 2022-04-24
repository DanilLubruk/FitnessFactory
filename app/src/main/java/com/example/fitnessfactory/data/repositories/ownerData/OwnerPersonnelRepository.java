package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.data.exceptions.NoDataException;
import com.example.fitnessfactory.data.models.Personnel;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.Single;

public abstract class OwnerPersonnelRepository extends BaseRepository {

    public Single<Boolean> isPersonnelOccupiedWithGyms(String personnelId) {
        return SingleCreate(emitter -> {
            Personnel personnel = Tasks.await(getCollection().document(personnelId).get()).toObject(Personnel.class);
            boolean isPersonnelOccupied = personnel.getGymsIds() != null && !personnel.getGymsIds().isEmpty();

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isPersonnelOccupied);
            }
        });
    }

    public Single<Boolean> isPersonnelWithThisIdAddedAsync(String userId) {
        return SingleCreate(emitter -> {
            boolean isAdminAdded = isPersonnelWithThisIdAdded(userId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isAdminAdded);
            }
        });
    }

    private boolean isPersonnelWithThisIdAdded(String userId) throws ExecutionException, InterruptedException {
        List<DocumentSnapshot> admins =
                Tasks.await(newQuery().whereUserIdEquals(userId).build().get()).getDocuments();
        return admins.size() > 0;
    }

    public Single<WriteBatch> getAddPersonnelBatchAsync(WriteBatch writeBatch, String userId) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getAddPersonnelBatch(writeBatch, userId));
            }
        });
    }

    private WriteBatch getAddPersonnelBatch(WriteBatch writeBatch, String userId) {
        DocumentReference documentReference = getCollection().document(userId);
        Personnel personnel = new Personnel();
        personnel.setUserId(userId);

        return writeBatch.set(documentReference, personnel);
    }

    public Single<WriteBatch> getDeletePersonnelBatchAsync(WriteBatch writeBatch, String userId) {
        return SingleCreate(emitter -> {
            WriteBatch deleteBatch = getDeletePersonnelBatch(writeBatch, userId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(deleteBatch);
            }
        });
    }

    private WriteBatch getDeletePersonnelBatch(WriteBatch writeBatch, String userId) throws Exception {
        return writeBatch.delete(getPersonnelDocumentReference(userId));
    }

    public Single<List<String>> getPersonnelIdsAsync() {
        return SingleCreate(emitter -> {
            List<String> personnelIds = getPersonnelIds();

            if (!emitter.isDisposed()) {
                emitter.onSuccess(personnelIds);
            }
        });
    }

    private List<String> getPersonnelIds() throws ExecutionException, InterruptedException {
        List<Personnel> personnelList = Tasks.await(getCollection().get()).toObjects(Personnel.class);

        List<String> personnelIds = new ArrayList<>();
        for (Personnel personnel : personnelList) {
            personnelIds.add(personnel.getUserId());
        }

        return personnelIds;
    }

    public Single<List<String>> getPersonnelIdsByGymIdAsync(String gymId) {
        return SingleCreate(emitter -> {
            List<String> personnelEmails = getPersonnelIdsByGymId(gymId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(personnelEmails);
            }
        });
    }

    private List<String> getPersonnelIdsByGymId(String gymId) throws ExecutionException, InterruptedException {
        List<Personnel> personnelList =
                Tasks.await(newQuery()
                        .wherePersonnelHasGym(gymId)
                        .build()
                        .get())
                        .toObjects(Personnel.class);

        List<String> personnelIds = new ArrayList<>();
        for (Personnel personnel : personnelList) {
            personnelIds.add(personnel.getUserId());
        }

        return personnelIds;
    }

    public Completable addGymToPersonnelAsync(String userId, String gymId) {
        return CompletableCreate(emitter -> {
            addGymToPersonnel(userId, gymId);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private void addGymToPersonnel(String userId, String gymId) throws Exception {
        Tasks.await(getPersonnelDocumentReference(userId)
                .update(Personnel.GYMS_ARRAY_FIELD, FieldValue.arrayUnion(gymId)));
    }

    public Completable removeGymFromPersonnelAsync(String userId, String gymId) {
        return CompletableCreate(emitter -> {
            removeGymFromPersonnel(userId, gymId);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private void removeGymFromPersonnel(String userId, String gymId) throws Exception {
        Tasks.await(getPersonnelDocumentReference(userId)
                .update(Personnel.GYMS_ARRAY_FIELD, FieldValue.arrayRemove(gymId)));
    }

    public Single<List<String>> getPersonnelGymsIdsByIdAsync(String userId) {
        return SingleCreate(emitter -> {
            List<String> gymsIds = getPersonnelGymsIdsById(userId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(gymsIds);
            }
        });
    }

    private List<String> getPersonnelGymsIdsById(String userId) throws Exception {
        List<String> personnelGymsIds;

        try {
            Personnel personnel = Tasks.await(getCollection().document(userId).get()).toObject(Personnel.class);
            if (personnel == null) {
                throw new NoDataException("");
            }
            personnelGymsIds =
                    personnel.getGymsIds() != null ?
                            personnel.getGymsIds() : new ArrayList<>();
        } catch (NoDataException noDataException) {
            personnelGymsIds = new ArrayList<>();
        }

        return personnelGymsIds;
    }

    public Single<WriteBatch> getRemoveGymFromPersonnelBatchAsync(WriteBatch writeBatch, String gymId) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getRemoveGymFromPersonnelBatch(writeBatch, gymId));
            }
        });
    }

    private WriteBatch getRemoveGymFromPersonnelBatch(WriteBatch writeBatch, String gymId)
            throws ExecutionException, InterruptedException {
        for (DocumentSnapshot documentSnapshot : getPersonnelContainingGym(gymId)) {
            writeBatch = writeBatch
                    .update(documentSnapshot.getReference(), Personnel.GYMS_ARRAY_FIELD, FieldValue.arrayRemove(gymId));
        }

        return writeBatch;
    }

    private List<DocumentSnapshot> getPersonnelContainingGym(String gymId) throws ExecutionException, InterruptedException {
        return Tasks.await(newQuery().wherePersonnelHasGym(gymId).build().get()).getDocuments();
    }

    private DocumentReference getPersonnelDocumentReference(String userId) {
        return getCollection().document(userId);
    }

    protected abstract OwnerPersonnelRepository.QueryBuilder newQuery();

    protected class QueryBuilder {

        private Query query = getCollection();

        public QueryBuilder whereUserIdEquals(String userId) {
            query = query.whereEqualTo(Personnel.USER_ID_FIELD, userId);
            return this;
        }

        public QueryBuilder wherePersonnelHasGym(String gymId) {
            query = query.whereArrayContains(Personnel.GYMS_ARRAY_FIELD, gymId);
            return this;
        }

        public Query build() {
            return query;
        }
    }
}
