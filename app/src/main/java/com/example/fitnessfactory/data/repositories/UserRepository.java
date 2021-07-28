package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.models.AppUser;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

import static com.example.fitnessfactory.data.models.AppUser.EMAIL_FILED;

public class UserRepository extends BaseRepository {

    @Override
    protected String getRoot() {
        return FirestoreCollections.USERS_COLLECTION;
    }

    public Single<Boolean> registerUser(String email) {
        return Single.create(emitter -> {
            DocumentReference documentReference = getCollection().document();
            AppUser appUser = new AppUser();
            appUser.setId(documentReference.getId());
            appUser.setEmail(email);
            try {
                Tasks.await(documentReference.set(appUser));
            } catch (Exception e) {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            }

            if (!emitter.isDisposed()) {
                emitter.onSuccess(true);
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
