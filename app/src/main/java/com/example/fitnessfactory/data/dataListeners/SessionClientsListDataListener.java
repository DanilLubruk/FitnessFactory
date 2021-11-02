package com.example.fitnessfactory.data.dataListeners;

import android.util.Log;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.SessionsClientsListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.ClientsCollection;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Single;

public class SessionClientsListDataListener extends BaseDataListener implements ArgDataListener<List<String>> {

    @Override
    protected String getRoot() {
        return ClientsCollection.getRoot();
    }

    public void startDataListener(List<String> clientsIds) {
        if (clientsIds == null || clientsIds.size() == 0) {
            return;
        }

        setListenerRegistration(getDataListener(clientsIds));
    }

    private Single<ListenerRegistration> getDataListener(List<String> clientsIds) {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getCollection()
                            .whereIn(Client.ID_FIELD, clientsIds)
                            .addSnapshotListener(((value, error) -> {
                                if (checkIsSnapshotValid(emitter, value, error)) {
                                    Log.d(AppConsts.DEBUG_TAG, "SessionClientsListDataListener: value null");
                                    return;
                                }

                                List<Client> clients = value.toObjects(Client.class);
                                EventBus.getDefault().post(new SessionsClientsListDataListenerEvent(clients));
                            }));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }
}
