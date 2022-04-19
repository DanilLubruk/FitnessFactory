package com.example.fitnessfactory.data.dataListeners;

import android.util.Log;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.DaysSessionListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.SessionsCollection;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.utils.TimeUtils;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import io.reactivex.Single;

public class DaysSessionsListDataListener extends BaseDataListener {

    @Override
    protected String getRoot() {
        return SessionsCollection.getRoot();
    }

    public void startDataListener(Date date) {
        setListenerRegistration(getDataListener(date));
    }

    private Single<ListenerRegistration> getDataListener(Date date) {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getCollection()
                            .whereGreaterThanOrEqualTo(Session.DATE_FIELD, TimeUtils.getStartOfDayDate(date).getTime())
                            .whereLessThanOrEqualTo(Session.DATE_FIELD, TimeUtils.getEndOfDayDate(date).getTime())
                            .addSnapshotListener(((value, error) -> {
                                if (checkIsSnapshotInvalid(emitter, value, error)) {
                                    Log.d(AppConsts.DEBUG_TAG, "DaysSessionsListDataListener: value null");
                                    return;
                                }

                                List<Session> sessions = value.toObjects(Session.class);
                                EventBus.getDefault().post(new DaysSessionListDataListenerEvent(sessions));
                            }));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }
}
