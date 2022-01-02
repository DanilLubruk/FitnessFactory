package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.data.firestoreCollections.OwnersCollection;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Owner;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.Single;

public class OwnersRepository extends BaseRepository {

    @Override
    protected String getRoot() {
        return OwnersCollection.getRoot();
    }

    public Single<AppUser> setOwnersIdAsync(AppUser appUser) {
        return SingleCreate(emitter -> {
            setOwnersId(appUser);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(appUser);
            }
        });
    }

    private void setOwnersId(AppUser appUser) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = getCollection().document(appUser.getId());
        Owner owner = new Owner();
        owner.setId(appUser.getId());
        Tasks.await(documentReference.set(owner));
    }
}
