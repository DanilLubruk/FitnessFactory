package com.example.fitnessfactory.data.repositories.access;

import com.example.fitnessfactory.data.firestoreCollections.AdminAccessCollection;
import com.example.fitnessfactory.data.models.AdminAccessEntry;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public class AdminsAccessRepository extends BaseRepository implements PersonnelAccessRepository {

    @Override
    public String getRoot() {
        return AdminAccessCollection.getRoot();
    }

    @Override
    public Single<Boolean> isPersonnelWithThisEmailRegistered(String ownerId, String email) {
        return SingleCreate(emitter -> {
            boolean isAdminRegistered = isAdminWithThisEmailRegistered(ownerId, email);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isAdminRegistered);
            }
        });
    }

    private boolean isAdminWithThisEmailRegistered(String ownerId, String userEmail) throws ExecutionException, InterruptedException {
        int adminsAmount =
                getEntitiesAmount(
                        newQuery()
                                .whereOwnerIdEquals(ownerId)
                                .whereUserEmailEquals(userEmail)
                                .build());

        return adminsAmount > 0;
    }

    @Override
    public Single<WriteBatch> getRegisterPersonnelAccessEntryBatch(String ownerId, String userEmail) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getRegisterAdminAccessBatchEntry(ownerId, userEmail));
            }
        });
    }

    private WriteBatch getRegisterAdminAccessBatchEntry(String ownerId, String email) {
        DocumentReference docReference = getCollection().document();
        AdminAccessEntry adminAccessEntry = new AdminAccessEntry();
        adminAccessEntry.setOwnerId(ownerId);
        adminAccessEntry.setUserEmail(email);

        return getFirestore().batch().set(docReference, adminAccessEntry);
    }

    @Override
    public Single<WriteBatch> getDeletePersonnelAccessEntryBatch(String ownerId, String email) {
        return SingleCreate(emitter -> {
            WriteBatch writeBatch = getDeleteAdminAccessBatchEntry(ownerId, email);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(writeBatch);
            }
        });
    }

    private WriteBatch getDeleteAdminAccessBatchEntry(String ownerId, String email) throws Exception {
        return getFirestore().batch().delete(getAdminDocument(ownerId, email));
    }

    public Single<List<String>> getOwnersByInvitedEmail(AppUser user) {
        return SingleCreate(emitter -> {
            List<String> ownerIds = new ArrayList<>();
            ownerIds.add(user.getId());
            ownerIds.addAll(getOwnerIds(user.getEmail()));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(ownerIds);
            }
        });
    }

    private List<String> getOwnerIds(String userEmail) throws Exception {
        List<String> ownerIds = new ArrayList<>();

        for (AdminAccessEntry adminAccessEntry : getAdminAccessEntriesByUserEmail(userEmail)) {
            ownerIds.add(adminAccessEntry.getOwnerId());
        }

        return ownerIds;
    }

    private List<AdminAccessEntry> getAdminAccessEntriesByUserEmail(String userEmail) throws Exception {
        return Tasks.await(
                newQuery()
                        .whereUserEmailEquals(userEmail)
                        .build()
                        .get())
                .toObjects(AdminAccessEntry.class);
    }

    private DocumentReference getAdminDocument(String ownerId, String email) throws Exception {
        return getAdminSnapshot(ownerId, email).getReference();
    }

    private DocumentSnapshot getAdminSnapshot(String ownerId, String email) throws Exception {
        return getUniqueUserEntitySnapshot(
                newQuery()
                        .whereOwnerIdEquals(ownerId)
                        .whereUserEmailEquals(email)
                        .build());
    }

    private AdminsAccessRepository.QueryBuilder newQuery() {
        return new AdminsAccessRepository().new QueryBuilder();
    }

    private class QueryBuilder {

        Query query = getCollection();

        public QueryBuilder whereUserEmailEquals(String userEmail) {
            query = query.whereEqualTo(AdminAccessEntry.USER_EMAIL_FIELD, userEmail);
            return this;
        }

        public QueryBuilder whereOwnerIdEquals(String ownerId) {
            query = query.whereEqualTo(AdminAccessEntry.OWNER_ID_FIELD, ownerId);
            return this;
        }

        public Query build() {
            return query;
        }
    }
}
