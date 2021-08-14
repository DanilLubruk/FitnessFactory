package com.example.fitnessfactory.data.repositories;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.events.AdminGymsListListenerEvent;
import com.example.fitnessfactory.data.events.GymsListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.BaseCollection;
import com.example.fitnessfactory.data.firestoreCollections.OwnerGymsCollection;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.google.firestore.v1.ListenRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public class GymRepository extends BaseRepository {

    @Override
    public String getRoot() {
        return super.getRoot() +
                "/" +
                FirestoreCollections.GYMS_COLLECTION +
                "/" +
                FirestoreCollections.GYMS_COLLECTION;
    }

    public Single<WriteBatch> getDeleteGymBatchAsync(String gymId) {
        return Single.create(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getDeleteGymBatchSync(gymId));
            }
        });
    }

    private WriteBatch getDeleteGymBatchSync(String gymId) {
        DocumentReference documentReference = getGymDocumentReference(gymId);

        return getFirestore().batch().delete(documentReference);
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
        Gym gym = new Gym();
        if (TextUtils.isEmpty(id)) {
            return gym;
        }

        DocumentSnapshot gymDoc;
        try {
            gymDoc = getGymDocSnapshot(id);
        } catch (InterruptedException e) {
            return gym;
        }

        return gymDoc.toObject(Gym.class);
    }

    private DocumentSnapshot getGymDocSnapshot(String id) throws Exception {
        QuerySnapshot gymsQuery = Tasks.await(getCollection().whereEqualTo(Gym.ID_FIELD, id).get());
        List<DocumentSnapshot> gymDocs = gymsQuery.getDocuments();

        checkUniqueness(gymDocs, getGymsIdUnuniqueErrorMessage());

        return gymDocs.get(0);
    }

    private String getGymsIdUnuniqueErrorMessage() {
        return ResUtils.getString(R.string.message_error_gyms_data_obtain)
                .concat(" - ")
                .concat(ResUtils.getString(R.string.message_error_ununique_id));
    }

    public Query getGymsListQuery() {
        return getCollection();
    }

    private DocumentReference getGymDocumentReference(String gymId) {
        return getCollection().document(gymId);
    }

    public Single<List<Gym>> getGymsByIds(List<String> gymIds) {
        return Single.create(emitter -> {
            List<Gym> gyms = getGymsByIds(emitter, gymIds);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(gyms);
            }
        });
    }

    private List<Gym> getGymsByIds(SingleEmitter<List<Gym>> emitter, List<String> gymIds) {
        List<Gym> gyms = new ArrayList<>();
        if (gymIds.size() == 0) {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(gyms);
            }
            return gyms;
        }

        try {
            gyms = Tasks.await(getCollection().whereIn(Gym.ID_FIELD, gymIds).get()).toObjects(Gym.class);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }

        return gyms;
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

    public String getGymNullErrorMessage() {
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
