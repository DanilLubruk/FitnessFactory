package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.FirestoreCollections;
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
import io.reactivex.CompletableEmitter;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public class AdminsRepository extends BaseRepository {

    @Override
    public String getRoot() {
        return OwnerAdminsCollection.getRoot();
    }

    public Single<WriteBatch> getRemoveGymFromAdminBatchAsync(WriteBatch writeBatch, String gymId) {
        return Single.create(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getRemoveGymFromAdminBatch(emitter, writeBatch, gymId));
            }
        });
    }

    private WriteBatch getRemoveGymFromAdminBatch(SingleEmitter<WriteBatch> emitter,
                                                  WriteBatch writeBatch,
                                                  String gymId) {
        WriteBatch removeBatch;

        try {
            removeBatch = getRemoveGymFromAdminBatch(writeBatch, gymId);
        } catch (InterruptedException e) {
            reportError(emitter, e);
            return null;
        } catch (Exception e) {
            reportError(emitter, e);
            return null;
        }

        return removeBatch;
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
        return Single.create(emitter -> {
            List<String> adminsEmails = getAdminsEmailsByGymId(emitter, gymId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(adminsEmails);
            }
        });
    }

    private List<String> getAdminsEmailsByGymId(SingleEmitter<List<String>> emitter, String gymId) {
        List<String> adminsEmails = new ArrayList<>();

        try {
            adminsEmails = getAdminsEmailsByGymId(gymId);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }

        return adminsEmails;
    }

    private List<String> getAdminsEmailsByGymId(String gymId) throws ExecutionException, InterruptedException {
        List<Admin> admins = Tasks.await(getAdminQueryByGymId(gymId).get()).toObjects(Admin.class);

        List<String> adminsEmails = new ArrayList<>();
        for (Admin admin : admins) {
            adminsEmails.add(admin.getUserEmail());
        }

        return adminsEmails;
    }

    public Single<List<String>> getAdminsGymsIdsAsync(String adminEmail) {
        return Single.create(emitter -> {
            List<String> gymsIds = getAdminsGymsIds(emitter, adminEmail);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(gymsIds);
            }
        });
    }

    private List<String> getAdminsGymsIds(SingleEmitter<List<String>> emitter, String adminEmail) {
        List<String> gymsIds = new ArrayList<>();

        try {
            gymsIds = getAdminsGymsIds(adminEmail);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }

        return gymsIds;
    }

    private List<String> getAdminsGymsIds(String adminEmail) throws Exception {
        Admin admin = getAdminSnapshot(adminEmail).toObject(Admin.class);
        if (admin == null) {
            throw new Exception();
        }

        return admin.getGymsIds();
    }

    public Completable removeGymFromAdminAsync(String adminEmail, String gymId) {
        return Completable.create(emitter -> {
            removeGymFromAdmin(emitter, adminEmail, gymId);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private void removeGymFromAdmin(CompletableEmitter emitter, String adminEmail, String gymId) {
        try {
            removeGymFromAdmin(adminEmail, gymId);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }
    }

    private void removeGymFromAdmin(String adminEmail, String gymId) throws Exception {
        Tasks.await(getAdminDocument(adminEmail).update(Admin.GYMS_ARRAY_FIELD, FieldValue.arrayRemove(gymId)));
    }

    public Completable addGymToAdminAsync(String adminEmail, String gymId) {
        return Completable.create(emitter -> {
            addGymToAdmin(emitter, adminEmail, gymId);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private void addGymToAdmin(CompletableEmitter emitter, String adminEmail, String gymId) {
        try {
            addGymToAdmin(adminEmail, gymId);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }
    }

    private void addGymToAdmin(String adminEmail, String gymId) throws Exception {
        Tasks.await(getAdminDocument(adminEmail).update(Admin.GYMS_ARRAY_FIELD, FieldValue.arrayUnion(gymId)));
    }

    public Single<WriteBatch> getDeleteAdminBatchAsync(WriteBatch writeBatch, String adminEmail) {
        return Single.create(emitter -> {
            WriteBatch deleteBatch = getDeleteAdminBatch(emitter, writeBatch, adminEmail);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(deleteBatch);
            }
        });
    }

    private WriteBatch getDeleteAdminBatch(SingleEmitter<WriteBatch> emitter,
                                           WriteBatch writeBatch,
                                           String adminEmail) {
        WriteBatch deleteBatch;

        try {
            deleteBatch = getDeleteAdminBatch(writeBatch, adminEmail);
        } catch (InterruptedException e) {
            reportError(emitter, e);
            return null;
        } catch (Exception e) {
            reportError(emitter, e);
            return null;
        }

        return deleteBatch;
    }

    private WriteBatch getDeleteAdminBatch(WriteBatch writeBatch, String adminEmail) throws Exception {
        return writeBatch.delete(getAdminDocument(adminEmail));
    }

    private DocumentReference getAdminDocument(String adminEmail) throws Exception {
        return getAdminSnapshot(adminEmail).getReference();
    }

    private DocumentSnapshot getAdminSnapshot(String adminEmail) throws Exception {
        List<DocumentSnapshot> documentSnapshots =
                Tasks.await(getAdminQueryByEmail(adminEmail).get()).getDocuments();

        checkEmailUniqueness(documentSnapshots);

        return documentSnapshots.get(0);
    }

    public Query getAdminQueryByEmail(String adminEmail) {
        return getCollection().whereEqualTo(Admin.USER_EMAIL_FIELD, adminEmail);
    }

    public Query getAdminQueryByGymId(String gymId) {
        return getCollection().whereArrayContains(Admin.GYMS_ARRAY_FIELD, gymId);
    }

    public Single<List<String>> getAdminsEmailsAsync() {
        return Single.create(emitter -> {
            List<String> adminsEmails = getAdminsEmails(emitter);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(adminsEmails);
            }
        });
    }

    private List<String> getAdminsEmails(SingleEmitter<List<String>> emitter) {
        List<String> adminsEmails = new ArrayList<>();

        try {
            adminsEmails = getAdminsEmails();
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }

        return adminsEmails;
    }

    private List<String> getAdminsEmails() throws ExecutionException, InterruptedException {
        List<Admin> admins = Tasks.await(getCollection().get()).toObjects(Admin.class);

        List<String> adminsEmails = new ArrayList<>();
        for (Admin admin : admins) {
            adminsEmails.add(admin.getUserEmail());
        }

        return adminsEmails;
    }

    public Single<WriteBatch> addAdminAsync(String userEmail, WriteBatch writeBatch) {
        return Single.create(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(addAdmin(userEmail, writeBatch));
            }
        });
    }

    private WriteBatch addAdmin(String userEmail, WriteBatch writeBatch) {
        DocumentReference documentReference = getCollection().document();
        Admin admin = new Admin();
        admin.setUserEmail(userEmail);

        return writeBatch.set(documentReference, admin);
    }

    public Single<Boolean> isAdminAddedAsync(String userEmail) {
        return Single.create(emitter -> {
            boolean isAdminAdded = isAdminAdded(emitter, userEmail);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isAdminAdded);
            }
        });
    }

    private boolean isAdminAdded(SingleEmitter<Boolean> emitter, String userEmail) {
        boolean isAdminAdded = false;
        try {
            isAdminAdded = isAdminAdded(userEmail);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }

        return isAdminAdded;
    }

    private boolean isAdminAdded(String userEmail) throws ExecutionException, InterruptedException {
        List<DocumentSnapshot> admins =
                Tasks.await(getCollection().whereEqualTo(Admin.USER_EMAIL_FIELD, userEmail).get()).getDocuments();
        boolean isAdminWithThisEmailAdded = admins.size() > 0;

        return isAdminWithThisEmailAdded;
    }
}
