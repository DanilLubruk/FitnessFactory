package com.example.fitnessfactory.data.repositories;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.events.GymsListDataListenerEvent;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.ListenRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.Single;

public class GymRepository extends BaseRepository {

    private ListenerRegistration gymsListListener;

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
            Gym gym = getGym(id);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(gym);
            }
        });
    }

    private Gym getGym(String id) throws Exception {
        if (TextUtils.isEmpty(id)) {
            return new Gym();
        }
        DocumentSnapshot gymDoc = getGymDocSnapshot(id);

        return gymDoc.toObject(Gym.class);
    }

    private DocumentSnapshot getGymDocSnapshot(String id) throws Exception {
        QuerySnapshot gymsQuery = Tasks.await(getCollection().whereEqualTo(Gym.ID_FIELD, id).get());
        List<DocumentSnapshot> gymDocs = gymsQuery.getDocuments();

        boolean isIdNotUnique = gymDocs.size() > 1;
        if (isIdNotUnique) {
            throw new Exception(getGymsIdUnuniqueErrorMessage());
        }

        return gymDocs.get(0);
    }

    private String getGymsIdUnuniqueErrorMessage() {
        return ResUtils.getString(R.string.message_error_gyms_data_obtain)
                .concat(" - ")
                .concat(ResUtils.getString(R.string.message_error_ununique_id));
    }

    public Completable addGymsListListener() {
        return Completable.create(source -> {
            gymsListListener = getCollection().addSnapshotListener((querySnapshot, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                    if (!source.isDisposed()) {
                        source.onError(exception);
                    }
                    return;
                }

                List<Gym> gyms = getGyms(querySnapshot);
                EventBus.getDefault().post(new GymsListDataListenerEvent(gyms));
                if (!source.isDisposed()) {
                    source.onComplete();
                }
            });
        });
    }

    private List<Gym> getGyms(QuerySnapshot querySnapshot) {
        List<Gym> gyms = new ArrayList<>();
        if (querySnapshot == null) {
            return gyms;
        }

        List<DocumentSnapshot> gymsDocs = querySnapshot.getDocuments();
        for (DocumentSnapshot document : gymsDocs) {
            gyms.add(document.toObject(Gym.class));
        }

        return gyms;
    }

    public Completable removeGymsListListener() {
        return Completable.create(source -> {
            if (gymsListListener != null) {
                gymsListListener.remove();
            }

            if (!source.isDisposed()) {
                source.onComplete();
            }
        });
    }

    public Single<Boolean> deleteSingle(Gym gym) {
        return Single.create(emitter -> {
            boolean isDeleted = deleteGym(gym);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isDeleted);
            }
        });
    }

    public Completable deleteCompletable(Gym gym) {
        return Completable.create(source -> {
            deleteGym(gym);

            if (!source.isDisposed()) {
                source.onComplete();
            }
        });
    }

    private boolean deleteGym(Gym gym) throws Exception {
        if (gym == null) {
            throw new Exception(getGymNullErrorMessage());
        }

        boolean isDeleted;
        DocumentReference docReference = getCollection().document(gym.getId());
        try {
            Tasks.await(docReference.delete());
            isDeleted = true;
        } catch (Exception e) {
            e.printStackTrace();
            isDeleted = false;
        }

        return isDeleted;
    }

    public Single<String> saveAsync(Gym gym) {
        return Single.create(emitter -> {
            String id = save(gym);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(id);
            }
        });
    }

    private String save(Gym gym) throws Exception {
        if (gym == null) {
            throw new Exception(getGymNullErrorMessage());
        }
        boolean isNewInstance = TextUtils.isEmpty(gym.getId());

        return isNewInstance ? insert(gym) : update(gym);
    }

    private String getGymNullErrorMessage() {
        return ResUtils.getString(R.string.message_error_gyms_data_save)
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
