package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.events.AdminGymsListListenerEvent;
import com.example.fitnessfactory.data.models.AdminAccessEntry;
import com.example.fitnessfactory.data.models.AppUser;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public class AdminsAccessRepository extends BaseRepository {

    @Override
    public String getRoot() {
        return FirestoreCollections.ADMINS_ACCESS_COLLECTION;
    }

    public Single<WriteBatch> getRegisterAdminAccessEntryBatchAsync(String ownerId, String userEmail) {
        return Single.create(emitter -> {
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

    public Single<Boolean> isAdminRegisteredAsync(String email) {
        return Single.create(emitter -> {
            boolean isAdminRegistered = isAdminRegistered(emitter, email);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isAdminRegistered);
            }
        });

    }

    private boolean isAdminRegistered(SingleEmitter<Boolean> emitter, String email) {
        boolean isAdminRegistered = false;
        try {
            isAdminRegistered = isAdminRegistered(email);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }

        return isAdminRegistered;
    }

    private boolean isAdminRegistered(String email) throws ExecutionException, InterruptedException {
        QuerySnapshot snapshot = Tasks.await(getCollection().whereEqualTo(AdminAccessEntry.USER_EMAIL_FIELD, email).get());
        boolean hasAdminAccessEntryWithThisEmail = snapshot.getDocuments().size() > 0;

        return hasAdminAccessEntryWithThisEmail;
    }

    public Single<List<String>> getOwnersByInvitedEmail(AppUser user) {
        return Single.create(emitter -> {
            List<String> ownerIds = new ArrayList<>();
            ownerIds.add(user.getId());
            ownerIds.addAll(getOwnerIds(emitter, user.getEmail()));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(ownerIds);
            }
        });
    }

    private List<String> getOwnerIds(SingleEmitter<List<String>> emitter, String userEmail) {
        List<String> ownerIds = new ArrayList<>();

        try {
            ownerIds = getOwnerIds(userEmail);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }

        return ownerIds;
    }

    private List<String> getOwnerIds(String userEmail) throws Exception {
        List<String> ownerIds = new ArrayList<>();

        for (DocumentSnapshot documentSnapshot : getOwnerIdsDocuments(userEmail)) {
            AdminAccessEntry adminAccessEntry = documentSnapshot.toObject(AdminAccessEntry.class);

            if (adminAccessEntry != null) {
                ownerIds.add(adminAccessEntry.getOwnerId());
            }
        }

        return ownerIds;
    }

    private List<DocumentSnapshot> getOwnerIdsDocuments(String userEmail) throws Exception {
        QuerySnapshot querySnapshot = Tasks.await(getCollection().whereEqualTo(AdminAccessEntry.USER_EMAIL_FIELD, userEmail).get());
        return querySnapshot.getDocuments();
    }

    public Single<WriteBatch> getDeleteAdminAccessEntryBatchAsync(String ownerId, String email) {
        return Single.create(emitter -> {
            WriteBatch writeBatch = getDeleteAdminAccessBatchEntry(emitter, ownerId, email);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(writeBatch);
            }
        });
    }

    private WriteBatch getDeleteAdminAccessBatchEntry(SingleEmitter<WriteBatch> emitter, String ownerId, String email) {
        WriteBatch writeBatch;

        try {
            writeBatch = getDeleteAdminAccessBatchEntry(ownerId, email);
        } catch (InterruptedException e) {
            reportError(emitter, e);
            return null;
        } catch (Exception e) {
            reportError(emitter, e);
            return null;
        }

        return writeBatch;
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

    public Query getAdminsListQuery(List<String> adminsEmails) throws Exception {
        if (adminsEmails.size() == 0) {
            throw new Exception();
        }

        return getCollection().whereIn(AdminAccessEntry.USER_EMAIL_FIELD, adminsEmails);
    }
}
