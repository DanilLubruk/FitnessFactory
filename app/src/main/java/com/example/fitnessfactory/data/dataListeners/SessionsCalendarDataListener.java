package com.example.fitnessfactory.data.dataListeners;

import android.util.Log;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.SessionsCalendarDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.SessionsCollection;
import com.example.fitnessfactory.data.models.Session;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import io.reactivex.Single;

public class SessionsCalendarDataListener extends BaseDataListener {

    @Override
    protected String getRoot() {
        return SessionsCollection.getRoot();
    }

    public void startDataListener(Date startDate, Date endDate) {
        setListenerRegistration(getDataListener(startDate, endDate));
    }

    private Single<ListenerRegistration> getDataListener(Date startDate, Date endDate) {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration = getCollection()
                    .whereGreaterThanOrEqualTo(Session.DATE_FIELD, startDate.getTime())
                    .whereLessThanOrEqualTo(Session.DATE_FIELD, endDate.getTime())
                    .addSnapshotListener((value, error) -> {
                        if (checkIsSnapshotInvalid(emitter, value, error)) {
                            Log.d(AppConsts.DEBUG_TAG, "SessionsCalendarDataListener: value null");
                            return;
                        }

                        List<Session> sessions = value.toObjects(Session.class);
                        EventBus.getDefault().post(new SessionsCalendarDataListenerEvent(sessions));
                    });

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }
}
