package com.example.fitnessfactory.data.dataListeners;

import android.util.Log;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.DaysSessionListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.SessionsCollection;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.utils.ResUtils;
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
                            .whereGreaterThan(Session.DATE_FIELD, TimeUtils.getStartOfDayDate(date))
                            .whereLessThan(Session.DATE_FIELD, TimeUtils.getEndOfDayDate(date))
                            .addSnapshotListener(((value, error) -> {
                                if (checkIsSnapshotValid(emitter, value, error)) {
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
