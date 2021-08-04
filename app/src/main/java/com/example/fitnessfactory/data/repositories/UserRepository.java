package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.models.AppUser;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;

import static com.example.fitnessfactory.data.models.AppUser.EMAIL_FILED;

public class UserRepository extends BaseRepository {

    @Override
    protected String getRoot() {
        return FirestoreCollections.USERS_COLLECTION;
    }

    public Single<String> registerUser(String email, String userName) {
        return Single.create(emitter -> {
            DocumentReference documentReference = getCollection().document();
            AppUser appUser = new AppUser();
            appUser.setId(documentReference.getId());
            appUser.setName(userName);
            appUser.setEmail(email);
            Tasks.await(documentReference.set(appUser));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(email);
            }
        });
    }

    public Single<List<AppUser>> getUsersAsync() {
        return Single.create(emitter -> {
           if (!emitter.isDisposed()) {
               emitter.onSuccess(getUsers());
           }
        });
    }

    public Single<List<AppUser>> getOwnersByIds(List<String> ownerIds) {
        return Single.create(emitter -> {
           List<AppUser> owners = new ArrayList<>();
           for (String ownerId : ownerIds) {
               AppUser appUser = getAppUser(ownerId);
               owners.add(appUser);
           }

           if (!emitter.isDisposed()) {
               emitter.onSuccess(owners);
           }
        });
    }

    private AppUser getAppUser(String userId) throws Exception {
        DocumentSnapshot document = Tasks.await(getCollection().document(userId).get());
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
        boolean isEmailNotUnique = documents.size() > 1;
        if (isEmailNotUnique) {
            throw new Exception();
        }

        DocumentSnapshot userSnapshot = documents.get(0);
        AppUser user = userSnapshot.toObject(AppUser.class);
        if (user == null) {
            throw new Exception();
        }

        AppPrefs.gymOwnerId().setValue(user.getId());
    }

    private List<AppUser> getUsers() throws ExecutionException, InterruptedException {
        List<AppUser> users = new ArrayList<>();
        QuerySnapshot usersQuery = Tasks.await(getCollection().orderBy(EMAIL_FILED).get());
        List<DocumentSnapshot> usersDocs = usersQuery.getDocuments();
        for (DocumentSnapshot userDoc : usersDocs) {
            users.add(userDoc.toObject(AppUser.class));
        }

        return users;
    }
}
