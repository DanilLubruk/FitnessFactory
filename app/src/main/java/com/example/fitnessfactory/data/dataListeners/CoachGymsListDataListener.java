package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.data.events.CoachGymsListListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.OwnerCoachesCollection;
import com.example.fitnessfactory.data.models.Personnel;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Single;

public class CoachGymsListDataListener extends BaseDataListener implements ArgDataListener<String> {

    @Override
    protected String getRoot() {
        return OwnerCoachesCollection.getRoot();
    }

    @Override
    public void startDataListener(String coachUserId) {
        setListenerRegistration(getDataListener(coachUserId));
    }

    private Single<ListenerRegistration> getDataListener(String coachUserId) {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getCollection().document(coachUserId)
                            .addSnapshotListener((value, error) -> {
                                if (checkIsSnapshotInvalid(emitter, error)) {
                                    return;
                                }

                                EventBus.getDefault().post(new CoachGymsListListenerEvent());
                            });

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }
}
