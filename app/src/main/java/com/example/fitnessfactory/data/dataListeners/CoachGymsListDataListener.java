package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.data.events.CoachGymsListListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.OwnerCoachesCollection;
import com.example.fitnessfactory.data.models.Personnel;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Single;

public class CoachGymsListDataListener extends BaseDataListener implements DataListenerStringArgument {

    @Override
    protected String getRoot() {
        return OwnerCoachesCollection.getRoot();
    }

    @Override
    public void startDataListener(String coachEmail) {
        setListenerRegistration(getDataListener(coachEmail));
    }

    private Single<ListenerRegistration> getDataListener(String coachEmail) {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getCoachQueryByEmail(coachEmail)
                            .addSnapshotListener((value, error) -> {
                                if (error != null) {
                                    reportError(emitter, error);
                                    return;
                                }

                                EventBus.getDefault().post(new CoachGymsListListenerEvent());
                            });

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }

    private Query getCoachQueryByEmail(String coachEmail) {
        return getCollection().whereEqualTo(Personnel.USER_EMAIL_FIELD, coachEmail);
    }
}
