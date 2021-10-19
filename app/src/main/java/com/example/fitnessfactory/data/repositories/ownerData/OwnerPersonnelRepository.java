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

public class OwnerPersonnelRepository extends BaseRepository {

    public Single<Boolean> isPersonnelWithThisEmailAddedAsync(String userEmail) {
        return SingleCreate(emitter -> {
            boolean isAdminAdded = isPersonnelWithThisEmailAdded(userEmail);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isAdminAdded);
            }
        });
    }

    private boolean isPersonnelWithThisEmailAdded(String userEmail) throws ExecutionException, InterruptedException {
        List<DocumentSnapshot> admins =
                Tasks.await(newQuery().whereUserEmailEquals(userEmail).build().get()).getDocuments();
        return admins.size() > 0;
    }

    public Single<WriteBatch> getAddPersonnelBatchAsync(WriteBatch writeBatch, String userEmail) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getAddPersonnelBatch(writeBatch, userEmail));
            }
        });
    }

    private WriteBatch getAddPersonnelBatch(WriteBatch writeBatch, String userEmail) {
        DocumentReference documentReference = getCollection().document();
        Personnel personnel = new Personnel();
        personnel.setUserEmail(userEmail);

        return writeBatch.set(documentReference, personnel);
    }

    public Single<WriteBatch> getDeletePersonnelBatchAsync(WriteBatch writeBatch, String personnelEmail) {
        return SingleCreate(emitter -> {
            WriteBatch deleteBatch = getDeletePersonnelBatch(writeBatch, personnelEmail);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(deleteBatch);
            }
        });
    }

    private WriteBatch getDeletePersonnelBatch(WriteBatch writeBatch, String personnelEmail) throws Exception {
        return writeBatch.delete(getPersonnelDocumentReference(personnelEmail));
    }

    public Single<List<String>> getPersonnelEmailsAsync() {
        return SingleCreate(emitter -> {
            List<String> personnelEmails = getPersonnelEmails();

            if (!emitter.isDisposed()) {
                emitter.onSuccess(personnelEmails);
            }
        });
    }

    private List<String> getPersonnelEmails() throws ExecutionException, InterruptedException {
        List<Personnel> personnelList = Tasks.await(getCollection().get()).toObjects(Personnel.class);

        List<String> personnelEmails = new ArrayList<>();
        for (Personnel personnel : personnelList) {
            personnelEmails.add(personnel.getUserEmail());
        }

        return personnelEmails;
    }

    public Single<List<String>> getPersonnelEmailsByGymIdAsync(String gymId) {
        return SingleCreate(emitter -> {
            List<String> personnelEmails = getPersonnelEmailsByGymId(gymId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(personnelEmails);
            }
        });
    }

    private List<String> getPersonnelEmailsByGymId(String gymId) throws ExecutionException, InterruptedException {
        List<Personnel> personnelList =
                Tasks.await(newQuery()
                        .wherePersonnelHasGym(gymId)
                        .build()
                        .get())
                        .toObjects(Personnel.class);

        List<String> personnelEmails = new ArrayList<>();
        for (Personnel personnel : personnelList) {
            personnelEmails.add(personnel.getUserEmail());
        }

        return personnelEmails;
    }

    public Completable addGymToPersonnelAsync(String personnelEmail, String gymId) {
        return CompletableCreate(emitter -> {
            addGymToAdminPersonnel(personnelEmail, gymId);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private void addGymToAdminPersonnel(String adminEmail, String gymId) throws Exception {
        Tasks.await(getPersonnelDocumentReference(adminEmail)
                .update(Personnel.GYMS_ARRAY_FIELD, FieldValue.arrayUnion(gymId)));
    }

    public Completable removeGymFromPersonnelAsync(String personnelEmail, String gymId) {
        return CompletableCreate(emitter -> {
            removeGymFromPersonnel(personnelEmail, gymId);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private void removeGymFromPersonnel(String personnelEmail, String gymId) throws Exception {
        Tasks.await(getPersonnelDocumentReference(personnelEmail)
                .update(Personnel.GYMS_ARRAY_FIELD, FieldValue.arrayRemove(gymId)));
    }

    public Single<List<String>> getPersonnelGymsIdsByEmailAsync(String adminEmail) {
        return SingleCreate(emitter -> {
            List<String> gymsIds = getPersonnelGymsIdsByEmail(adminEmail);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(gymsIds);
            }
        });
    }

    private List<String> getPersonnelGymsIdsByEmail(String personnelEmail) throws Exception {
        List<String> personnelGymsIds;

        try {
            Personnel personnel = getUniquePersonnelEntity(personnelEmail);
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

    private DocumentReference getPersonnelDocumentReference(String adminEmail) throws Exception {
        return getPersonnelSnapshot(adminEmail).getReference();
    }

    private Personnel getUniquePersonnelEntity(String personnelEmail) throws Exception {
        return getUniqueUserEntity(newQuery().whereUserEmailEquals(personnelEmail).build(), Personnel.class);
    }

    private DocumentSnapshot getPersonnelSnapshot(String adminEmail) throws Exception {
        return getUniqueUserEntitySnapshot(newQuery().whereUserEmailEquals(adminEmail).build());
    }

    private OwnerPersonnelRepository.QueryBuilder newQuery() {
        return new OwnerPersonnelRepository().new QueryBuilder();
    }

    private class QueryBuilder {

        Query query = getCollection();

        public QueryBuilder whereUserEmailEquals(String userEmail) {
            query = query.whereEqualTo(Personnel.USER_EMAIL_FIELD, userEmail);
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
