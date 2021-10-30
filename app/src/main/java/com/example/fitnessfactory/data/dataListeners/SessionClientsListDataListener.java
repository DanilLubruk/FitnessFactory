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

public class SessionClientsListDataListener extends BaseDataListener implements DataListenerStringArgument {

    @Override
    protected String getRoot() {
        return ClientsCollection.getRoot();
    }

    @Override
    public void startDataListener(String sessionId) {
        setListenerRegistration(getDataListener(sessionId));
    }

    private Single<ListenerRegistration> getDataListener(String sessionId) {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getCollection()
                            .whereArrayContains(Client.SESSIONS_IDS_FIELD, sessionId)
                            .addSnapshotListener(((value, error) -> {
                                if (error != null) {
                                    reportError(emitter, error);
                                    return;
                                }
                                if (value == null) {
                                    Log.d(AppConsts.DEBUG_TAG, "SessionClientsListDataListener: value null");
                                    reportError(emitter, new Exception(ResUtils.getString(R.string.message_error_data_obtain)));
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
