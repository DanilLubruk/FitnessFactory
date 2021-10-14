package com.example.fitnessfactory.data.repositories.ownerData;

import android.text.TextUtils;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.firestoreCollections.OwnerGymsCollection;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public class OwnerGymRepository extends BaseRepository {

    @Override
    public String getRoot() {
        return OwnerGymsCollection.getRoot();
    }

    public Single<WriteBatch> getDeleteGymBatchAsync(String gymId) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getDeleteGymBatchSync(gymId));
            }
        });
    }

    private WriteBatch getDeleteGymBatchSync(String gymId) {
        DocumentReference documentReference = getGymDocumentReference(gymId);

        return getFirestore().batch().delete(documentReference);
    }

    public Single<Gym> getGymAsync(String gymId) {
        return SingleCreate(emitter -> {
            Gym gym = getGym(gymId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(gym);
            }
        });
    }

    private Gym getGym(String gymId) throws Exception {
        if (TextUtils.isEmpty(gymId)) {
            return new Gym();
        }

        return getUniqueEntity(
                getCollection().whereEqualTo(Gym.ID_FIELD, gymId),
                Gym.class,
                getGymsIdUnuniqueErrorMessage());
    }

    private String getGymsIdUnuniqueErrorMessage() {
        return ResUtils.getString(R.string.message_error_data_obtain)
                .concat(" - ")
                .concat(ResUtils.getString(R.string.message_error_ununique_id));
    }

    private DocumentReference getGymDocumentReference(String gymId) {
        return getCollection().document(gymId);
    }

    public Single<List<Gym>> getGymsByIdsAsync(List<String> gymIds) {
        return SingleCreate(emitter -> {
            List<Gym> gyms = getGymsByIds(gymIds);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(gyms);
            }
        });
    }

    private List<Gym> getGymsByIds(List<String> gymIds) throws ExecutionException, InterruptedException {
        if (gymIds.size() == 0) {
            return new ArrayList<>();
        }

        return Tasks.await(getCollection().whereIn(Gym.ID_FIELD, gymIds).get()).toObjects(Gym.class);
    }

    public Single<String> saveAsync(Gym gym) {
        return SingleCreate(emitter -> {
            String gymId = save(gym);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(gymId);
            }
        });
    }

    private String save(Gym gym) throws Exception {
        if (gym == null) {
            throw new Exception(getGymNullErrorMessage());
        }

        return isNewGym(gym.getId()) ? insert(gym) : update(gym);
    }

    private boolean isNewGym(String gymId) throws Exception {
        int gymsNumber = getGymsNumberWithThatId(gymId);

        return gymsNumber == 0;
    }

    private int getGymsNumberWithThatId(String gymId) throws Exception {
        List<Gym> gyms =
                Tasks.await(
                getCollection().whereEqualTo(Gym.ID_FIELD, gymId).get())
                .toObjects(Gym.class);
        checkUniqueness(gyms, getGymsIdUnuniqueErrorMessage());

        return gyms.size();
    }

    private String getGymNullErrorMessage() {
        return ResUtils.getString(R.string.message_error_data_save)
                .concat(" - ")
                .concat(ResUtils.getString(R.string.message_error_gym_null));
    }

    private String insert(Gym gym) throws ExecutionException, InterruptedException {
        DocumentReference docReference = getCollection().document();
        gym.setId(docReference.getId());
        Tasks.await(docReference.set(gym));

        return gym.getId();
    }

    private String update(Gym gym) throws ExecutionException, InterruptedException {
        Tasks.await(getCollection().document(gym.getId()).set(gym));

        return gym.getId();
    }
}
