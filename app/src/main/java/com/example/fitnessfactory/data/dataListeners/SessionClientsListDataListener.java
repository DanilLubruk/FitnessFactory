package com.example.fitnessfactory.data.dataListeners;

import android.util.Log;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.SessionsClientsListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.SessionsCollection;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Single;

public class SessionClientsListDataListener extends BaseDataListener implements ArgDataListener<String> {

    @Override
    protected String getRoot() {
        return SessionsCollection.getRoot();
    }

    public void startDataListener(String sessionId) {
        setListenerRegistration(getDataListener(sessionId));
    }

    private Single<ListenerRegistration> getDataListener(String sessionId) {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getCollection()
                            .whereEqualTo(Session.ID_FIELD, sessionId)
                            .addSnapshotListener(((value, error) -> {
                                if (checkIsSnapshotInvalid(emitter, value, error)) {
                                    Log.d(AppConsts.DEBUG_TAG, "SessionClientsListDataListener: value null");
                                    return;
                                }
                                List<Session> sessions = value.toObjects(Session.class);
                                try {
                                    checkUniqueness(sessions, getUnuniqueSessionMessage());
                                    checkDataEmpty(sessions);
                                } catch (Exception e) {
                                    reportError(emitter, e);
                                    return;
                                }

                                Session session = sessions.get(0);
                                EventBus.getDefault().post(new SessionsClientsListDataListenerEvent(session.getClientsIds()));
                            }));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }

    private String getUnuniqueSessionMessage() {
        return ResUtils.getString(R.string.message_error_session_id_not_unique);
    }
}
