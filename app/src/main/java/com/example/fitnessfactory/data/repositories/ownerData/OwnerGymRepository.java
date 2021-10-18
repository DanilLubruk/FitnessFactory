package com.example.fitnessfactory.data.repositories.ownerData;

import android.text.TextUtils;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.firestoreCollections.OwnerGymsCollection;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
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

    public Single<Boolean> saveAsync(Gym gym) {
        return SingleCreate(emitter -> {
            boolean isSaved = save(gym);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isSaved);
            }
        });
    }

    private boolean save(Gym gym) throws Exception {
        if (gym == null) {
            throw new Exception(getGymNullErrorMessage());
        }
        boolean isNewGym = StringUtils.isEmpty(gym.getId());

        return isNewGym ? insert(gym) : update(gym);
    }

    private String getGymNullErrorMessage() {
        return ResUtils.getString(R.string.message_error_data_save)
                .concat(" - ")
                .concat(ResUtils.getString(R.string.message_error_gym_null));
    }

    private boolean insert(Gym gym) throws Exception {
        if (isNewGymDuplicate(gym)) {
            throw new Exception(getGymDuplicateMessage());
        }
        DocumentReference docReference = getCollection().document();
        gym.setId(docReference.getId());
        Tasks.await(docReference.set(gym));

        return true;
    }

    private boolean update(Gym gym) throws Exception {
        if (isExistingGymDuplicate(gym)) {
            throw new Exception(getGymDuplicateMessage());
        }

        Tasks.await(getCollection().document(gym.getId()).set(gym));

        return true;
    }

    private boolean isNewGymDuplicate(Gym gym) throws ExecutionException, InterruptedException {
        int gymsAmount = getGymsAmount(OwnerGymRepository.newBuilder()
                .whereNameEquals(gym.getName())
                .whereAddressEquals(gym.getAddress())
                .build());

        return gymsAmount > 0;
    }

    private boolean isExistingGymDuplicate(Gym gym) throws ExecutionException, InterruptedException {
        int gymsAmount = getGymsAmount(OwnerGymRepository.newBuilder()
                .whereIdNotEquals(gym.getId())
                .whereNameEquals(gym.getName())
                .whereAddressEquals(gym.getAddress())
                .build());

        return gymsAmount > 0;
    }

    private int getGymsAmount(Query query) throws ExecutionException, InterruptedException {
        List<Gym> gyms = Tasks.await(query.get()).toObjects(Gym.class);

        return gyms.size();
    }

    private String getGymDuplicateMessage() {
        return ResUtils.getString(R.string.message_gym_duplicate);
    }

    public static OwnerGymRepository.QueryBuilder newBuilder() {
        return new OwnerGymRepository().new QueryBuilder();
    }

    public class QueryBuilder {

        Query query = getCollection();

        public QueryBuilder whereIdNotEquals(String gymId) {
            query = query.whereNotEqualTo(Gym.ID_FIELD, gymId);
            return this;
        }

        public QueryBuilder whereNameEquals(String gymName) {
            query = query.whereEqualTo(Gym.NAME_FILED, gymName);
            return this;
        }

        public QueryBuilder whereAddressEquals(String gymAddress) {
            query = query.whereEqualTo(Gym.ADDRESS_FIELD, gymAddress);
            return this;
        }

        public Query build() {
            return query;
        }
    }
}
