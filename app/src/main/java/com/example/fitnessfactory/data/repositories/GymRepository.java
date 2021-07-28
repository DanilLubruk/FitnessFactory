package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.models.Gym;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public class GymRepository extends BaseRepository {

    @Override
    protected String getRoot() {
        return super.getRoot() +
                FirestoreCollections.GYMS_COLLECTION +
                "/" +
                FirestoreCollections.GYMS_COLLECTION;
    }

    public Single<List<Gym>> getGymsAsync() {
        return Single.create(emitter -> {
           if (!emitter.isDisposed()) {
               emitter.onSuccess(getGyms());
           }
        });
    }

    private List<Gym> getGyms() throws ExecutionException, InterruptedException {
        List<Gym> gyms = new ArrayList<>();
        QuerySnapshot gymsQuery = Tasks.await(getCollection().orderBy(Gym.NAME_FILED).get());
        List<DocumentSnapshot> gymsDocs = gymsQuery.getDocuments();
        for (DocumentSnapshot document : gymsDocs) {
            gyms.add(document.toObject(Gym.class));
        }

        return gyms;
    }
}
