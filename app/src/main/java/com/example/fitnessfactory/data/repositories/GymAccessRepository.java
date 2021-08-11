package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.models.AccessEntry;
import com.example.fitnessfactory.data.models.Gym;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class GymAccessRepository extends BaseRepository {

    @Inject
    GymRepository gymRepository;

    @Inject
    StaffAccessRepository accessRepository;

    public GymAccessRepository() {
        FFApp.get().getAppComponent().inject(this);
    }

    private FirebaseFirestore getFirestore() {
        return FirebaseFirestore.getInstance();
    }

    public Completable deleteGymCompletable(Gym gym) {
        return Completable.create(emitter -> {
           deleteGym(gym);

           if (!emitter.isDisposed()) {
               emitter.onComplete();
           }
        });
    }

    public Single<Boolean> deleteGymSingle(Gym gym) {
        return Single.create(emitter -> {
           boolean isDeleted = deleteGym(gym);

           if (!emitter.isDisposed()) {
               emitter.onSuccess(isDeleted);
           }
        });
    }

    private boolean deleteGym(Gym gym) throws Exception {
        if (gym == null) {
            throw new Exception(gymRepository.getGymNullErrorMessage());
        }

        boolean isDeleted = Tasks.await(getFirestore().runTransaction(transaction -> {
            DocumentReference gymDocumentRef = gymRepository.getGymDocumentReference(gym);
            List<DocumentReference> personnelWithGym;
            try {
                personnelWithGym = accessRepository.getPersonnelWithGymInList(gym.getId());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            for (DocumentReference documentReference : personnelWithGym) {
                try {
                    AccessEntry entry = Tasks.await(documentReference.get()).toObject(AccessEntry.class);
                    transaction.update(
                            documentReference,
                            FirestoreCollections.GYMS_COLLECTION,
                            FieldValue.arrayRemove(gym.getId()));
                    entry = Tasks.await(documentReference.get()).toObject(AccessEntry.class);
                } catch (Exception e) {

                }
            }
            transaction.delete(gymDocumentRef);

            return true;
        }));

        return isDeleted;
    }
}
