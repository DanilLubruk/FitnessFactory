package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.events.AdminGymsListListenerEvent;
import com.example.fitnessfactory.data.events.AdminsListDataListenerEvent;
import com.example.fitnessfactory.data.models.AdminAccessEntry;
import com.example.fitnessfactory.data.models.AppUser;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public class AdminsAccessRepository extends BaseRepository {

    @Override
    public String getRoot() {
        return FirestoreCollections.ADMINS_ACCESS_COLLECTION;
    }

    public Single<WriteBatch> registerAdminAccessEntryAsync(String ownerId, String userEmail) {
        return Single.create(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(registerAdminAccessEntry(ownerId, userEmail));
            }
        });
    }

    private WriteBatch registerAdminAccessEntry(String ownerId, String email) {
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

    public Single<List<String>> getAdminsEmailsByOwnerIdAsync(String ownerId) {
        return Single.create(emitter -> {
            List<String> adminEmails = getAdminsEmailsByOwnerId(emitter, ownerId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(adminEmails);
            }
        });
    }

    private List<String> getAdminsEmailsByOwnerId(SingleEmitter<List<String>> emitter, String ownerId) {
        List<String> adminEmails = new ArrayList<>();
        try {
            adminEmails = getAdminsEmailsByOwnerId(ownerId);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }

        return adminEmails;
    }

    private List<String> getAdminsEmailsByOwnerId(String ownerId) throws Exception {
        List<String> emails = new ArrayList<>();

        for (DocumentSnapshot documentSnapshot : getAdminsEmailsDocuments(ownerId)) {
            AdminAccessEntry admin = documentSnapshot.toObject(AdminAccessEntry.class);
            if (admin != null) {
                emails.add(admin.getUserEmail());
            }
        }

        return emails;
    }

    private List<DocumentSnapshot> getAdminsEmailsDocuments(String ownerId) throws Exception {
        QuerySnapshot snapshot = Tasks.await(getCollection().whereEqualTo(AdminAccessEntry.OWNER_ID_FIELD, ownerId).get());
        return snapshot.getDocuments();
    }

    public Single<WriteBatch> deleteAdminAccessEntryAsync(String ownerId, String email) {
        return Single.create(emitter -> {
            WriteBatch writeBatch = deleteAdminAccessEntry(emitter, ownerId, email);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(writeBatch);
            }
        });
    }

    private WriteBatch deleteAdminAccessEntry(SingleEmitter<WriteBatch> emitter, String ownerId, String email) {
        WriteBatch writeBatch;

        try {
            writeBatch = deleteAdminAccessEntry(ownerId, email);
        } catch (InterruptedException e) {
            reportError(emitter, e);
            return null;
        } catch (Exception e) {
            reportError(emitter, e);
            return null;
        }

        return writeBatch;
    }

    private WriteBatch deleteAdminAccessEntry(String ownerId, String email) throws Exception {
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

    public Query getAdminQueryByGymId(String ownerId, String gymId) {
        return getCollection()
                .whereEqualTo(AdminAccessEntry.OWNER_ID_FIELD, ownerId)
                .whereArrayContains(AdminAccessEntry.GYMS_FIELD, gymId);
    }

    public Single<List<String>> getAdminGymsAsync(String ownerId, String personnelEmail) {
        return Single.create(emitter -> {
            List<String> personnelGyms = getAdminGyms(emitter, ownerId, personnelEmail);
            if (personnelGyms == null) {
                personnelGyms = new ArrayList<>();
            }

            if (!emitter.isDisposed()) {
                emitter.onSuccess(personnelGyms);
            }
        });
    }

    private List<String> getAdminGyms(SingleEmitter<List<String>> emitter,
                                      String ownerId,
                                      String personnelEmail) {
        List<String> personnelGyms = new ArrayList<>();

        try {
            personnelGyms = getAdminGyms(ownerId, personnelEmail);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }

        return personnelGyms;
    }

    private List<String> getAdminGyms(String ownerId, String personnelEmail) throws Exception {
        AdminAccessEntry personnel = getAdmin(ownerId, personnelEmail);

        return personnel.getGyms();
    }

    private AdminAccessEntry getAdmin(String ownerId, String email) throws Exception {
        DocumentSnapshot documentSnapshot = getAdminSnapshot(ownerId, email);

        return documentSnapshot.toObject(AdminAccessEntry.class);
    }

    public Single<ListenerRegistration> getAdminsListListener(List<String> adminsEmails) {
        return Single.create(emitter -> {
            if (adminsEmails.size() == 0) {
                if (!emitter.isDisposed()) {
                    emitter.onError(new Exception());
                }
            }

            ListenerRegistration adminsListListener =
                    getCollection()
                            .whereIn(AdminAccessEntry.USER_EMAIL_FIELD, adminsEmails)
                            .addSnapshotListener(((value, error) -> {
                                if (error != null) {
                                    reportError(emitter, error);
                                }

                                EventBus.getDefault().post(new AdminsListDataListenerEvent());
                            }));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(adminsListListener);
            }
        });
    }

    public Completable addAdminGymsListListener(String idOwner, String email) {
        return Completable.create(emitter -> {
            adminGymsListListener = getAdminQueryByEmail(idOwner, email)
                    .addSnapshotListener((snapshot, error) -> {
                        if (error != null) {
                            reportError(emitter, error);
                            return;
                        }

                        EventBus.getDefault().post(new AdminGymsListListenerEvent());
                    });

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    public Completable removeAdminGymsListListener() {
        return Completable.create(emitter -> {
            if (adminGymsListListener != null) {
                adminGymsListListener.remove();
            }

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    public Completable addGymToPersonnelAsync(String idOwner, String email, String gymId) {
        return Completable.create(emitter -> {
            addGymToPersonnel(emitter, idOwner, email, gymId);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private void addGymToPersonnel(CompletableEmitter emitter,
                                   String idOwner,
                                   String email,
                                   String gymId) {
        try {
            addGymToPersonnel(idOwner, email, gymId);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }
    }

    private void addGymToPersonnel(String idOwner, String email, String gymId) throws Exception {
        DocumentReference documentReference = getAdminDocument(idOwner, email);
        Tasks.await(
                documentReference.update(
                        FirestoreCollections.GYMS_COLLECTION,
                        FieldValue.arrayUnion(gymId)));
    }

    public Completable removeGymFromPersonnelAsync(String idOwner, String email, String gymId) {
        return Completable.create(emitter -> {
            removeGymFromPersonnel(emitter, idOwner, email, gymId);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private void removeGymFromPersonnel(CompletableEmitter emitter,
                                        String idOwner,
                                        String email,
                                        String gymId) {
        try {
            removeGymFromPersonnel(idOwner, email, gymId);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }
    }

    private void removeGymFromPersonnel(String idOwner, String email, String gymId) throws Exception {
        DocumentReference documentReference = getAdminDocument(idOwner, email);
        Tasks.await(
                documentReference.update(
                        FirestoreCollections.GYMS_COLLECTION,
                        FieldValue.arrayRemove(gymId)));
    }

    public List<DocumentReference> getPersonnelWithGymInList(String gymId) throws ExecutionException, InterruptedException {
        List<DocumentReference> personnel = new ArrayList<>();
        List<DocumentSnapshot> documentSnapshots =
                Tasks.await(getCollection().whereArrayContains(FirestoreCollections.GYMS_COLLECTION, gymId).get()).getDocuments();

        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
            personnel.add(documentSnapshot.getReference());
        }

        return personnel;
    }

    public List<String> getAdminEmailsByGymId(String ownerId, String gymId) throws Exception {
        List<AdminAccessEntry> admins =
                Tasks.await(
                        getAdminQueryByGymId(ownerId, gymId)
                                .get())
                        .toObjects(AdminAccessEntry.class);

        return getEmailsFromAccessEntries(admins);
    }

    private List<String> getEmailsFromAccessEntries(List<AdminAccessEntry> entries) {
        List<String> emails = new ArrayList<>();
        for (AdminAccessEntry adminAccessEntry : entries) {
            emails.add(adminAccessEntry.getUserEmail());
        }

        return emails;
    }
}
