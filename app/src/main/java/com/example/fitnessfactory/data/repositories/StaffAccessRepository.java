package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.events.AdminGymsListListenerEvent;
import com.example.fitnessfactory.data.events.AdminsListDataListenerEvent;
import com.example.fitnessfactory.data.models.AccessEntry;
import com.example.fitnessfactory.data.models.AppUser;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public class StaffAccessRepository extends BaseRepository {

    private ListenerRegistration adminsListListener;
    private ListenerRegistration adminGymsListListener;

    @Override
    public String getRoot() {
        return FirestoreCollections.ACCESS_COLLECTION;
    }

    public Completable registerAccess(String userEmail, String ownerId) {
        return Completable.create(source -> {
            DocumentReference docReference = getCollection().document();
            AccessEntry accessEntry = new AccessEntry();
            accessEntry.setUserEmail(userEmail);
            accessEntry.setOwnerId(ownerId);

            Tasks.await(docReference.set(accessEntry));

            if (!source.isDisposed()) {
                source.onComplete();
            }
        });
    }

    public Single<Boolean> isAccessRegistered(String email) {
        return Single.create(emitter -> {
            QuerySnapshot snapshot = Tasks.await(getCollection().whereEqualTo(AccessEntry.USER_EMAIL_FIELD, email).get());
            boolean hasAccessEntryWithThisEmail = snapshot.getDocuments().size() > 0;

            if (!emitter.isDisposed()) {
                emitter.onSuccess(hasAccessEntryWithThisEmail);
            }
        });

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
            AccessEntry accessEntry = documentSnapshot.toObject(AccessEntry.class);

            if (accessEntry != null) {
                ownerIds.add(accessEntry.getOwnerId());
            }
        }

        return ownerIds;
    }

    private List<DocumentSnapshot> getOwnerIdsDocuments(String userEmail) throws Exception {
        QuerySnapshot querySnapshot = Tasks.await(getCollection().whereEqualTo(AccessEntry.USER_EMAIL_FIELD, userEmail).get());
        return querySnapshot.getDocuments();
    }

    public Single<List<String>> getAdminsEmailsByOwnerId(String ownerId) {
        return Single.create(emitter -> {
            List<String> adminEmails = getAdminsEmails(emitter, ownerId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(adminEmails);
            }
        });
    }

    private List<String> getAdminsEmails(SingleEmitter<List<String>> emitter, String ownerId) {
        List<String> adminEmails = new ArrayList<>();
        try {
            adminEmails = getAdminsEmails(ownerId);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }

        return adminEmails;
    }

    private List<String> getAdminsEmails(String ownerId) throws Exception {
        List<String> emails = new ArrayList<>();

        for (DocumentSnapshot documentSnapshot : getAdminsEmailsDocuments(ownerId)) {
            AccessEntry admin = documentSnapshot.toObject(AccessEntry.class);
            if (admin != null) {
                emails.add(admin.getUserEmail());
            }
        }

        return emails;
    }

    private List<DocumentSnapshot> getAdminsEmailsDocuments(String ownerId) throws Exception {
        QuerySnapshot snapshot = Tasks.await(getCollection().whereEqualTo(AccessEntry.OWNER_ID_FIELD, ownerId).get());
        return snapshot.getDocuments();
    }

    public Completable deleteAdminCompletable(String ownerId, String email) {
        return Completable.create(emitter -> {
            deleteAdmin(emitter, ownerId, email);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private void deleteAdmin(CompletableEmitter emitter, String ownerId, String email) {
        try {
            deleteAdmin(ownerId, email);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }
    }

    private void deleteAdmin(String ownerId, String email) throws Exception {
        DocumentReference docReference = getPersonnelDocument(ownerId, email);
        Tasks.await(docReference.delete());
    }

    private DocumentReference getPersonnelDocument(String ownerId, String email) throws Exception {
        return getPersonnelSnapshot(ownerId, email).getReference();
    }

    private DocumentSnapshot getPersonnelSnapshot(String ownerId, String email) throws Exception {
        List<DocumentSnapshot> documents =
                Tasks.await(getPersonnelQuery(ownerId, email).get())
                        .getDocuments();

        checkEmailUniqueness(documents);

        return documents.get(0);
    }

    private Query getPersonnelQuery(String ownerId, String email) {
        return getCollection()
                .whereEqualTo(AccessEntry.OWNER_ID_FIELD, ownerId)
                .whereEqualTo(AccessEntry.USER_EMAIL_FIELD, email);
    }

    public Single<List<String>> getPersonnelGymsAsync(String ownerId, String personnelEmail) {
        return Single.create(emitter -> {
           List<String> personnelGyms = getPersonnelGyms(emitter, ownerId, personnelEmail);
           if (personnelGyms == null) {
               personnelGyms = new ArrayList<>();
           }

           if (!emitter.isDisposed()) {
               emitter.onSuccess(personnelGyms);
           }
        });
    }

    private List<String> getPersonnelGyms(SingleEmitter<List<String>> emitter,
                                          String ownerId,
                                          String personnelEmail) {
        List<String> personnelGyms = new ArrayList<>();

        try {
            personnelGyms = getPersonnelGyms(ownerId, personnelEmail);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }

        return personnelGyms;
    }

    private List<String> getPersonnelGyms(String ownerId, String personnelEmail) throws Exception {
        AccessEntry personnel = getPersonnel(ownerId, personnelEmail);

        return personnel.getGyms();
    }

    private AccessEntry getPersonnel(String ownerId, String email) throws Exception {
        DocumentSnapshot documentSnapshot = getPersonnelSnapshot(ownerId, email);

        return documentSnapshot.toObject(AccessEntry.class);
    }

    public Completable addAdminsListListener(String ownerId) {
        return Completable.create(emitter -> {
            adminsListListener = getCollection().whereEqualTo(AccessEntry.OWNER_ID_FIELD, ownerId)
                    .addSnapshotListener((snapshot, error) -> {
                        if (error != null) {
                            if (!emitter.isDisposed()) {
                                emitter.onError(error);
                            }
                            return;
                        }

                        EventBus.getDefault().post(new AdminsListDataListenerEvent());
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    });
        });
    }

    public Completable removeAdminsListListener() {
        return Completable.create(emitter -> {
           if (adminsListListener != null) {
               adminsListListener.remove();
           }

           if (!emitter.isDisposed()) {
               emitter.onComplete();
           }
        });
    }

    public Completable addAdminGymsListListener(String idOwner, String email) {
        return Completable.create(emitter -> {
            adminGymsListListener = getPersonnelQuery(idOwner, email)
                    .addSnapshotListener((snapshot, error) -> {
                        if (error != null) {
                            reportError(emitter, error);
                            return;
                        }

                        EventBus.getDefault().post(new AdminGymsListListenerEvent());
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    });
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
        DocumentReference documentReference = getPersonnelDocument(idOwner, email);
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
        DocumentReference documentReference = getPersonnelDocument(idOwner, email);
        Tasks.await(
                documentReference.update(
                        FirestoreCollections.GYMS_COLLECTION,
                        FieldValue.arrayRemove(gymId)));
    }
}
