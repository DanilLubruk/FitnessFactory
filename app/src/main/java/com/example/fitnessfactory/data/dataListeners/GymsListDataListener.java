package com.example.fitnessfactory.data.dataListeners;

import android.util.Log;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.GymsListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.OwnerGymsCollection;
import com.example.fitnessfactory.data.models.Gym;
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

    public void startDataListener() {
        setListenerRegistration(getDataListener());
    }

    private Single<ListenerRegistration> getDataListener() {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getGymsListQuery()
                            .addSnapshotListener(((value, error) -> {
                                if (checkIsSnapshotInvalid(emitter, value, error)) {
                                    Log.d(AppConsts.DEBUG_TAG, "GymsListDataListener: value null");
                                    return;
                                }

                                List<Gym> gyms = value.toObjects(Gym.class);
                                EventBus.getDefault().post(new GymsListDataListenerEvent(gyms));
                            }));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }

    private Query getGymsListQuery() {
        return getCollection();
    }
}
