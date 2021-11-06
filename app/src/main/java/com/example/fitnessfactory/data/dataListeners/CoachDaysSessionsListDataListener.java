package com.example.fitnessfactory.data.dataListeners;

import android.util.Log;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.CoachDaysSessionsListDataListenerEvent;
import com.example.fitnessfactory.data.events.DaysSessionListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.SessionsCollection;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.utils.TimeUtils;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import io.reactivex.Single;

public class CoachDaysSessionsListDataListener extends BaseDataListener {

    @Override
    protected String getRoot() {
        return SessionsCollection.getRoot();
    }

    public void startDataListener(Date date, String coachId) {
        setListenerRegistration(getDataListener(date, coachId));
    }

    private Single<ListenerRegistration> getDataListener(Date date, String coachId) {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getCollection()
                            .whereGreaterThan(Session.DATE_FIELD, TimeUtils.getStartOfDayDate(date))
                            .whereLessThan(Session.DATE_FIELD, TimeUtils.getEndOfDayDate(date))
                            .whereArrayContains(Session.COACHES_IDS_FIELD, coachId)
                            .addSnapshotListener(((value, error) -> {
                                if (checkIsSnapshotInvalid(emitter, value, error)) {
                                    Log.d(AppConsts.DEBUG_TAG, "CoachDaysSessionsListDataListener: value null");
                                    return;
                                }

                                List<Session> sessions = value.toObjects(Session.class);
                                EventBus.getDefault().post(new CoachDaysSessionsListDataListenerEvent(sessions));
                            }));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }
}
