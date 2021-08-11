package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.events.AdminsListDataListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;

import static com.example.fitnessfactory.data.models.AppUser.EMAIL_FILED;

import org.greenrobot.eventbus.EventBus;

public class UserRepository extends BaseRepository {

    @Override
    protected String getRoot() {
        return FirestoreCollections.USERS_COLLECTION;
    }

    public Single<AppUser> registerUser(String email, String userName) {
        return Single.create(emitter -> {
            DocumentReference documentReference = getCollection().document();
            AppUser appUser = new AppUser();
            appUser.setId(documentReference.getId());
            appUser.setName(userName);
            appUser.setEmail(email);
            Tasks.await(documentReference.set(appUser));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(appUser);
            }
        });
    }

    public Single<List<AppUser>> getOwnersByIds(List<String> ownerIds) {
        return Single.create(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getAppUsersById(ownerIds));
            }
        });
    }

    private List<AppUser> getAppUsersById(List<String> ownerIds) throws Exception {
        QuerySnapshot querySnapshot = Tasks.await(getCollection().whereIn(AppUser.ID_FIELD, ownerIds).get());

        return querySnapshot.toObjects(AppUser.class);
    }

    public Single<AppUser> getAppUserByEmailAsync(String email) {
        return Single.create(emitter -> {
            AppUser appUser = getAppUserByEmail(email);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(appUser);
            }
        });
    }

    private AppUser getAppUserByEmail(String email) throws Exception {
        QuerySnapshot querySnapshot = Tasks.await(getCollection().whereEqualTo(EMAIL_FILED, email).get());
        List<DocumentSnapshot> documents = querySnapshot.getDocuments();

        checkEmailUniqueness(documents);

        DocumentSnapshot document = documents.get(0);
        return document.toObject(AppUser.class);
    }

    public Single<Boolean> isRegistered(String email) {
        return Single.create(emitter -> {
            getCollection().whereEqualTo(EMAIL_FILED, email).get()
                    .addOnSuccessListener(snapshot -> {
                        if (snapshot == null) {
                            if (!emitter.isDisposed()) {
                                emitter.onSuccess(false);
                            }
                        }


                        List<DocumentSnapshot> documents = snapshot.getDocuments();
                        boolean isRegistered = documents.size() > 0;

                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(isRegistered);
                        }
                    })
                    .addOnFailureListener(exception -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(exception);
                        }
                    });
        });
    }

    public Single<Boolean> saveUserId(String email) {
        return Single.create(emitter -> {
            getCollection().whereEqualTo(EMAIL_FILED, email).get()
                    .addOnSuccessListener(snapshot -> {
                        saveUserId(snapshot, emitter);

                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(true);
                        }
                    })
                    .addOnFailureListener(exception -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(exception);
                        }
                    });
        });
    }

    private void saveUserId(QuerySnapshot snapshot, SingleEmitter<Boolean> emitter) {
        try {
            saveUserId(snapshot);
        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(false);
            }
        }
    }

    private void saveUserId(QuerySnapshot snapshot) throws Exception {
        if (snapshot == null) {
            throw new Exception();
        }

        List<DocumentSnapshot> documents = snapshot.getDocuments();
        checkEmailUniqueness(documents);

        DocumentSnapshot userSnapshot = documents.get(0);
        AppUser user = userSnapshot.toObject(AppUser.class);
        if (user == null) {
            throw new Exception();
        }

        AppPrefs.gymOwnerId().setValue(user.getId());
    }

    public Single<List<AppUser>> getAdminsByEmails(List<String> adminsEmails) {
        return Single.create(emitter -> {
            List<AppUser> admins = getAdminsList(emitter, adminsEmails);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(admins);
            }
        });
    }

    private List<AppUser> getAdminsList(SingleEmitter<List<AppUser>> emitter, List<String> adminsEmails) {
        List<AppUser> admins = new ArrayList<>();
        try {
            admins = getAdminsList(adminsEmails);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }

        return admins;
    }

    public List<AppUser> getAdminsList(List<String> adminsEmails) throws Exception {
        if (adminsEmails.size() == 0) {
            return new ArrayList<>();
        }
        QuerySnapshot adminsQuery = Tasks.await(getCollection().whereIn(EMAIL_FILED, adminsEmails).get());

        return adminsQuery.toObjects(AppUser.class);
    }
}
