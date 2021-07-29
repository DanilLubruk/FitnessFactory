package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.utils.ResUtils;
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
                "/" +
                FirestoreCollections.GYMS_COLLECTION +
                "/" +
                FirestoreCollections.GYMS_COLLECTION;
    }

    public Single<Gym> getGymAsync(String id) {
        return Single.create(emitter -> {
           if (!emitter.isDisposed()) {
               emitter.onSuccess(getGym(id));
           }
        });
    }

    private Gym getGym(String id) throws Exception {
        QuerySnapshot gymsQuery = Tasks.await(getCollection().whereEqualTo(Gym.ID_FIELD, id).get());
        List<DocumentSnapshot> gymDocs = gymsQuery.getDocuments();
        boolean isIdNotUnique = gymDocs.size() > 1;
        if (isIdNotUnique) {
            throw new Exception(
                    ResUtils.getString(R.string.message_error_gyms_data_obtain)
                            .concat(" ")
                            .concat(ResUtils.getString(R.string.message_error_ununique_id)));
        }
        DocumentSnapshot gymDoc = gymDocs.get(0);

        return gymDoc.toObject(Gym.class);
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
