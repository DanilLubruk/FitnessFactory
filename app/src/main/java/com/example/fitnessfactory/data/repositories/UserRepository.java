package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.firestoreCollections.UsersCollection;
import com.example.fitnessfactory.data.models.AppUser;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

import static com.example.fitnessfactory.data.models.AppUser.EMAIL_FILED;

public class UserRepository extends BaseRepository {

    @Override
    public String getRoot() {
        return UsersCollection.getRoot();
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

    public Single<List<AppUser>> getUsersByEmailsAsync(List<String> emails) {
        return Single.create(emitter -> {
            List<AppUser> admins = getUsersByEmails(emitter, emails);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(admins);
            }
        });
    }

    private List<AppUser> getUsersByEmails(SingleEmitter<List<AppUser>> emitter, List<String> emails) {
        List<AppUser> users = new ArrayList<>();
        try {
            users = getUsersByEmails(emails);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }

        return users;
    }

    private List<AppUser> getUsersByEmails(List<String> emails) throws Exception {
        if (emails.size() == 0) {
            return new ArrayList<>();
        }
        QuerySnapshot adminsQuery = Tasks.await(getCollection().whereIn(EMAIL_FILED, emails).get());

        return adminsQuery.toObjects(AppUser.class);
    }
}
