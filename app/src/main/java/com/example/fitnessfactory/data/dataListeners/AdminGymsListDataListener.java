package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.data.events.AdminGymsListListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.OwnerAdminsCollection;
import com.example.fitnessfactory.data.models.Personnel;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import org.greenrobot.eventbus.EventBus;


import io.reactivex.Single;

public class AdminGymsListDataListener extends BaseDataListener implements ArgDataListener<String> {

    @Override
    protected String getRoot() {
        return OwnerAdminsCollection.getRoot();
    }

    @Override
    public void startDataListener(String adminUserId) {
        setListenerRegistration(getDataListener(adminUserId));
    }

    private Single<ListenerRegistration> getDataListener(String adminUserId) {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getCollection().document(adminUserId)
                            .addSnapshotListener(((value, error) -> {
                                if (checkIsSnapshotInvalid(emitter, error)) {
                                    return;
                                }

                                EventBus.getDefault().post(new AdminGymsListListenerEvent());
                            }));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }
}
