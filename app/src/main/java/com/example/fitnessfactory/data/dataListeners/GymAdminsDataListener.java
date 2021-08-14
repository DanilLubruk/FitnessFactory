package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.events.GymAdminsListListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.OwnerAdminsCollection;
import com.example.fitnessfactory.data.models.Admin;
import com.example.fitnessfactory.utils.RxUtils;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import org.greenrobot.eventbus.EventBus;
import io.reactivex.Single;

public class GymAdminsDataListener extends BaseDataListener {

    @Override
    protected String getRoot() {
        return OwnerAdminsCollection.getRoot();
    }

    public void setGymAdminsDataListener(String gymId) {
        getGymAdminsListener(gymId)
                .subscribeOn(getIOScheduler())
                .observeOn(getIOScheduler())
                .subscribe(dataListener::set, RxUtils::handleError)
                .dispose();
    }

    private Single<ListenerRegistration> getGymAdminsListener(String gymId) {
        return Single.create(emitter -> {
           ListenerRegistration listenerRegistration =
                   getAdminQueryByGymId(gymId)
                           .addSnapshotListener(((value, error) -> {
                               if (error != null) {
                                   reportError(emitter, error);
                               }

                               EventBus.getDefault().post(new GymAdminsListListenerEvent());
                           }));

           if (!emitter.isDisposed()) {
               emitter.onSuccess(listenerRegistration);
           }
        });
    }

    public Query getAdminQueryByGymId(String gymId) {
        return getCollection().whereArrayContains(Admin.GYMS_ARRAY_FIELD, gymId);
    }
}
