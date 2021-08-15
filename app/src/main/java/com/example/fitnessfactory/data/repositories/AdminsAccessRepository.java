package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.firestoreCollections.AdminAccessCollection;
import com.example.fitnessfactory.data.models.AdminAccessEntry;
import com.example.fitnessfactory.data.models.AppUser;
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

public class AdminsAccessRepository extends BaseRepository {

    @Override
    public String getRoot() {
        return AdminAccessCollection.getRoot();
    }

    public Single<WriteBatch> getRegisterAdminAccessEntryBatchAsync(String ownerId, String userEmail) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getRegisterAdminAccessBatchEntry(ownerId, userEmail));
            }
        });
    }

    private WriteBatch getRegisterAdminAccessBatchEntry(String ownerId, String email) {
        DocumentReference docReference = getCollection().document();
        AdminAccessEntry adminAccessEntry = new AdminAccessEntry();
        adminAccessEntry.setUserEmail(email);
        adminAccessEntry.setOwnerId(ownerId);

        return getFirestore().batch().set(docReference, adminAccessEntry);
    }

    public Single<Boolean> isAdminWithThisEmailRegisteredAsync(String email) {
        return SingleCreate(emitter -> {
            boolean isAdminRegistered = isAdminWithThisEmailRegistered(email);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isAdminRegistered);
            }
        });
    }

    private boolean isAdminWithThisEmailRegistered(String email) throws ExecutionException, InterruptedException {
        QuerySnapshot snapshot = Tasks.await(getCollection().whereEqualTo(AdminAccessEntry.USER_EMAIL_FIELD, email).get());

        return snapshot.getDocuments().size() > 0;
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
                getCollection()
                        .whereEqualTo(AdminAccessEntry.USER_EMAIL_FIELD, userEmail).get())
                .toObjects(AdminAccessEntry.class);
    }

    public Single<WriteBatch> getDeleteAdminAccessEntryBatchAsync(String ownerId, String email) {
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

    private DocumentReference getAdminDocument(String ownerId, String email) throws Exception {
        return getAdminSnapshot(ownerId, email).getReference();
    }

    private DocumentSnapshot getAdminSnapshot(String ownerId, String email) throws Exception {
        List<DocumentSnapshot> documents =
                Tasks.await(getAdminQueryByEmail(ownerId, email).get())
                        .getDocuments();

        checkEmailUniqueness(documents);

        return documents.get(0);
    }

    public Query getAdminQueryByEmail(String ownerId, String email) {
        return getCollection()
                .whereEqualTo(AdminAccessEntry.OWNER_ID_FIELD, ownerId)
                .whereEqualTo(AdminAccessEntry.USER_EMAIL_FIELD, email);
    }
}
