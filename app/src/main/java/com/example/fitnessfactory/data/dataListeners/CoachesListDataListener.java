package com.example.fitnessfactory.data.dataListeners;
import com.example.fitnessfactory.data.events.CoachesListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.OwnerCoachesCollection;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Single;

public class CoachesListDataListener extends BaseDataListener implements DataListener {

    @Override
    protected String getRoot() {
        return OwnerCoachesCollection.getRoot();
    }

    public void startDataListener() {
        setListenerRegistration(getDataListener());
    }

    private Single<ListenerRegistration> getDataListener() {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getCollection().addSnapshotListener((value, error) -> {
                        if (error != null) {
                            reportError(emitter, error);
                        }

                        EventBus.getDefault().post(new CoachesListDataListenerEvent());
                    });

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }
}
