package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.data.events.GymAdminsListListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.OwnerAdminsCollection;
import com.example.fitnessfactory.data.models.Personnel;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import org.greenrobot.eventbus.EventBus;
import io.reactivex.Single;

public class GymAdminsListDataListener extends BaseDataListener implements ArgDataListener<String> {

    @Override
    protected String getRoot() {
        return OwnerAdminsCollection.getRoot();
    }

    public void startDataListener(String gymId) {
        setListenerRegistration(getDataListener(gymId));
    }

    private Single<ListenerRegistration> getDataListener(String gymId) {
        return Single.create(emitter -> {
           ListenerRegistration listenerRegistration =
                   getAdminQueryByGymId(gymId)
                           .addSnapshotListener(((value, error) -> {
                               if (checkIsSnapshotInvalid(emitter, error)) {
                                   return;
                               }

                               EventBus.getDefault().post(new GymAdminsListListenerEvent());
                           }));

           if (!emitter.isDisposed()) {
               emitter.onSuccess(listenerRegistration);
           }
        });
    }

    public Query getAdminQueryByGymId(String gymId) {
        return getCollection().whereArrayContains(Personnel.GYMS_ARRAY_FIELD, gymId);
    }
}
