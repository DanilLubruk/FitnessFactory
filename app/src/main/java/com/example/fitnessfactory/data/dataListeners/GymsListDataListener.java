package com.example.fitnessfactory.data.dataListeners;
import com.example.fitnessfactory.data.events.GymsListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.OwnerGymsCollection;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.utils.RxUtils;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import io.reactivex.Single;

public class GymsListDataListener extends BaseDataListener {

    @Override
    protected String getRoot() {
        return OwnerGymsCollection.getRoot();
    }

    public void setGymsListListener() {
       addSubscription(getGymsListListener()
                .subscribeOn(getIOScheduler())
                .observeOn(getMainThreadScheduler())
                .subscribe(dataListener::set, RxUtils::handleError));
    }

    private Single<ListenerRegistration> getGymsListListener() {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getGymsListQuery()
                            .addSnapshotListener(((value, error) -> {
                                if (error != null) {
                                    reportError(emitter, error);
                                }

                                if (value != null) {
                                    List<Gym> gyms = value.toObjects(Gym.class);
                                    EventBus.getDefault().post(new GymsListDataListenerEvent(gyms));
                                }
                            }));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }

    public Query getGymsListQuery() {
        return getCollection();
    }
}
