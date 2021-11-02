package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.data.events.GymCoachesListListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.OwnerCoachesCollection;
import com.example.fitnessfactory.data.models.Personnel;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Single;

public class GymCoachesListDataListener  extends BaseDataListener implements ArgDataListener<String> {

    @Override
    protected String getRoot() {
        return OwnerCoachesCollection.getRoot();
    }

    public void startDataListener(String gymId) {
        setListenerRegistration(getDataListener(gymId));
    }

    private Single<ListenerRegistration> getDataListener(String gymId) {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getCoachQueryByGymId(gymId)
                            .addSnapshotListener(((value, error) -> {
                                if (checkIsSnapshotValid(emitter, error)) {
                                    return;
                                }

                                EventBus.getDefault().post(new GymCoachesListListenerEvent());
                            }));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }

    public Query getCoachQueryByGymId(String gymId) {
        return getCollection().whereArrayContains(Personnel.GYMS_ARRAY_FIELD, gymId);
    }
}
