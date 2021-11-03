package com.example.fitnessfactory.data.dataListeners;

import android.util.Log;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.ClientsListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.ClientsCollection;
import com.example.fitnessfactory.data.models.Client;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Single;

public class ClientsListDataListener extends BaseDataListener {

    @Override
    protected String getRoot() {
        return ClientsCollection.getRoot();
    }

    public void startDataListener() {
        setListenerRegistration(getDataListener());
    }

    public Single<ListenerRegistration> getDataListener() {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getCollection().addSnapshotListener(((value, error) -> {
                        if (checkIsSnapshotInvalid(emitter, value, error)) {
                            Log.d(AppConsts.DEBUG_TAG, "ClientsListDataListener: value null");
                            return;
                        }

                        List<Client> clientsList = value.toObjects(Client.class);
                        EventBus.getDefault().post(new ClientsListDataListenerEvent(clientsList));
                    }));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }
}
