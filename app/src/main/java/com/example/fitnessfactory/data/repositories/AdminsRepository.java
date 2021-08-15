package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.firestoreCollections.OwnerAdminsCollection;
import com.example.fitnessfactory.data.models.Admin;
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

public class AdminsRepository extends BaseRepository {

    @Override
    public String getRoot() {
        return OwnerAdminsCollection.getRoot();
    }

    public Single<WriteBatch> getRemoveGymFromAdminBatchAsync(WriteBatch writeBatch, String gymId) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getRemoveGymFromAdminBatch(writeBatch, gymId));
            }
        });
    }

    private WriteBatch getRemoveGymFromAdminBatch(WriteBatch writeBatch, String gymId) throws ExecutionException, InterruptedException {
        for (DocumentSnapshot documentSnapshot : getAdminsListSnapshotsByGymId(gymId)) {
            writeBatch = writeBatch
                    .update(
                            documentSnapshot.getReference(),
                            Admin.GYMS_ARRAY_FIELD,
                            FieldValue.arrayRemove(gymId));
        }

        return writeBatch;
    }

    private List<DocumentSnapshot> getAdminsListSnapshotsByGymId(String gymId) throws ExecutionException, InterruptedException {
        return Tasks.await(getCollection().whereArrayContains(Admin.GYMS_ARRAY_FIELD, gymId).get()).getDocuments();
    }

    public Single<List<String>> getAdminsEmailsByGymIdAsync(String gymId) {
        return SingleCreate(emitter -> {
            List<String> adminsEmails = getAdminsEmailsByGymId(gymId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(adminsEmails);
            }
        });
    }

    private List<String> getAdminsEmailsByGymId(String gymId) throws ExecutionException, InterruptedException {
        List<Admin> admins = Tasks.await(getAdminQueryByGymId(gymId).get()).toObjects(Admin.class);

        List<String> adminsEmails = new ArrayList<>();
        for (Admin admin : admins) {
            adminsEmails.add(admin.getUserEmail());
        }

        return adminsEmails;
    }

    public Single<List<String>> getAdminsGymsEmailAsync(String adminEmail) {
        return SingleCreate(emitter -> {
            List<String> gymsIds = getAdminsGymsByEmail(adminEmail);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(gymsIds);
            }
        });
    }

    private List<String> getAdminsGymsByEmail(String adminEmail) throws Exception {
        List<String> adminGymsIds = new ArrayList<>();

        List<Admin> admins = Tasks.await(
                getCollection().whereEqualTo(Admin.USER_EMAIL_FIELD, adminEmail).get()).toObjects(Admin.class);
        if (admins.isEmpty()) {
            return adminGymsIds;
        }

        checkEmailUniqueness(admins);

        Admin admin = admins.get(0);
        if (admin.getGymsIds() != null) {
            adminGymsIds = admin.getGymsIds();
        }

        return adminGymsIds;
    }

    public Completable removeGymFromAdminAsync(String adminEmail, String gymId) {
        return CompletableCreate(emitter -> {
            removeGymFromAdmin(adminEmail, gymId);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private void removeGymFromAdmin(String adminEmail, String gymId) throws Exception {
        Tasks.await(getAdminDocumentReference(adminEmail).update(Admin.GYMS_ARRAY_FIELD, FieldValue.arrayRemove(gymId)));
    }

    public Completable addGymToAdminAsync(String adminEmail, String gymId) {
        return CompletableCreate(emitter -> {
            addGymToAdmin(adminEmail, gymId);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private void addGymToAdmin(String adminEmail, String gymId) throws Exception {
        Tasks.await(getAdminDocumentReference(adminEmail).update(Admin.GYMS_ARRAY_FIELD, FieldValue.arrayUnion(gymId)));
    }

    public Single<WriteBatch> getDeleteAdminBatchAsync(WriteBatch writeBatch, String adminEmail) {
        return SingleCreate(emitter -> {
            WriteBatch deleteBatch = getDeleteAdminBatch(writeBatch, adminEmail);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(deleteBatch);
            }
        });
    }

    private WriteBatch getDeleteAdminBatch(WriteBatch writeBatch, String adminEmail) throws Exception {
        return writeBatch.delete(getAdminDocumentReference(adminEmail));
    }

    private DocumentReference getAdminDocumentReference(String adminEmail) throws Exception {
        return getAdminSnapshot(adminEmail).getReference();
    }

    private DocumentSnapshot getAdminSnapshot(String adminEmail) throws Exception {
        List<DocumentSnapshot> documentSnapshots =
                Tasks.await(getCollection().whereEqualTo(Admin.USER_EMAIL_FIELD, adminEmail).get()).getDocuments();

        checkEmailUniqueness(documentSnapshots);

        checkDataEmpty(documentSnapshots);

        return documentSnapshots.get(0);
    }

    public Query getAdminQueryByGymId(String gymId) {
        return getCollection().whereArrayContains(Admin.GYMS_ARRAY_FIELD, gymId);
    }

    public Single<List<String>> getAdminsEmailsAsync() {
        return SingleCreate(emitter -> {
            List<String> adminsEmails = getAdminsEmails();

            if (!emitter.isDisposed()) {
                emitter.onSuccess(adminsEmails);
            }
        });
    }

    private List<String> getAdminsEmails() throws ExecutionException, InterruptedException {
        List<Admin> admins = Tasks.await(getCollection().get()).toObjects(Admin.class);

        List<String> adminsEmails = new ArrayList<>();
        for (Admin admin : admins) {
            adminsEmails.add(admin.getUserEmail());
        }

        return adminsEmails;
    }

    public Single<WriteBatch> getAddAdminBatchAsync(String userEmail, WriteBatch writeBatch) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getAddAdminBatch(userEmail, writeBatch));
            }
        });
    }

    private WriteBatch getAddAdminBatch(String userEmail, WriteBatch writeBatch) {
        DocumentReference documentReference = getCollection().document();
        Admin admin = new Admin();
        admin.setUserEmail(userEmail);

        return writeBatch.set(documentReference, admin);
    }

    public Single<Boolean> isAdminWithThisEmailAddedAsync(String userEmail) {
        return SingleCreate(emitter -> {
            boolean isAdminAdded = isAdminWithThisEmailAdded(userEmail);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isAdminAdded);
            }
        });
    }

    private boolean isAdminWithThisEmailAdded(String userEmail) throws ExecutionException, InterruptedException {
        List<DocumentSnapshot> admins =
                Tasks.await(getCollection().whereEqualTo(Admin.USER_EMAIL_FIELD, userEmail).get()).getDocuments();
        return admins.size() > 0;
    }
}
