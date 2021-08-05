package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.events.AdminsListDataListenerEvent;
import com.example.fitnessfactory.data.models.AccessEntry;
import com.example.fitnessfactory.data.models.AppUser;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public class StaffAccessRepository extends BaseRepository {

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
            ownerIds.addAll(getOwnerIdsByInvitedEmail(user.getEmail()));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(ownerIds);
            }
        });
    }

    private List<String> getOwnerIdsByInvitedEmail(String userEmail) throws Exception {
        QuerySnapshot querySnapshot = Tasks.await(getCollection().whereEqualTo(AccessEntry.USER_EMAIL_FIELD, userEmail).get());
        List<DocumentSnapshot> documents = querySnapshot.getDocuments();

        return getOwnerIds(documents);
    }

    private List<String> getOwnerIds(List<DocumentSnapshot> documents) {
        List<String> ownerIds = new ArrayList<>();

        for (DocumentSnapshot documentSnapshot : documents) {
            AccessEntry accessEntry = documentSnapshot.toObject(AccessEntry.class);

            if (accessEntry != null) {
                ownerIds.add(accessEntry.getOwnerId());
            }
        }

        return ownerIds;
    }

    public Single<List<String>> getAdminsEmailsByOwnerId(String ownerId) {
        return Single.create(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getAdminsEmails(ownerId));
            }
        });
    }

    private List<String> getAdminsEmails(String ownerId) throws Exception {
        QuerySnapshot snapshot;
        try {
            snapshot = Tasks.await(getCollection().whereEqualTo(AccessEntry.OWNER_ID_FIELD, ownerId).get());
        } catch (InterruptedException e) {
            return new ArrayList<>();
        }
        List<DocumentSnapshot> documents = snapshot.getDocuments();
        List<String> emails = new ArrayList<>();
        for (DocumentSnapshot documentSnapshot : documents) {
            AccessEntry admin = documentSnapshot.toObject(AccessEntry.class);
            if (admin != null) {
                emails.add(admin.getUserEmail());
            }
        }

        return emails;
    }
}
