package com.example.fitnessfactory.data.dataListeners;

import android.util.Log;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.SessionTypesListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.SessionTypesCollection;
import com.example.fitnessfactory.data.models.SessionType;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Single;

public class SessionTypesListDataListener extends BaseDataListener {

    @Override
    protected String getRoot() {
        return SessionTypesCollection.getRoot();
    }

    public void startDataListener() {
        setListenerRegistration(getDataListener());
    }

    private Single<ListenerRegistration> getDataListener() {
        return Single.create(emitter -> {
           ListenerRegistration listenerRegistration = getCollection()
                   .addSnapshotListener((value, error) -> {
                       if (checkIsSnapshotInvalid(emitter, value, error)) {
                           Log.d(AppConsts.DEBUG_TAG, "SessionTypesListDataListener: value null");
                           return;
                       }

                       List<SessionType> sessionTypes = value.toObjects(SessionType.class);
                       EventBus.getDefault().post(new SessionTypesListDataListenerEvent(sessionTypes));
                   });

           if (!emitter.isDisposed()) {
               emitter.onSuccess(listenerRegistration);
           }
        });
    }
}
