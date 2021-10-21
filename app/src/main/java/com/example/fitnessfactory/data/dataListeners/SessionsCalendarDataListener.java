package com.example.fitnessfactory.data.dataListeners;

import android.util.Log;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.SessionsCalendarDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.SessionsCollection;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.firebase.Timestamp;
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
                    .whereGreaterThan(Session.DATE_FIELD, startDate)
                    .whereLessThan(Session.DATE_FIELD, endDate)
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            reportError(emitter, error);
                            return;
                        }
                        if (value == null) {
                            Log.d(AppConsts.DEBUG_TAG, "SessionsCalendarDataListener: value null");
                            reportError(emitter, new Exception(ResUtils.getString(R.string.message_error_data_obtain)));
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
