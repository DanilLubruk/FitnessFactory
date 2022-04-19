package com.example.fitnessfactory.data.dataListeners;

import android.util.Log;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.CoachDaysSessionsListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.SessionsCollection;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.utils.TimeUtils;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;

public class CoachDaysSessionsListDataListener extends BaseDataListener {

    @Override
    protected String getRoot() {
        return SessionsCollection.getRoot();
    }

    public void startDataListener(Date date, String coachEmail) {
        setListenerRegistration(getDataListener(date, coachEmail));
    }

    private Single<ListenerRegistration> getDataListener(Date date, String coachEmail) {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getCollection()
                            .whereGreaterThanOrEqualTo(Session.DATE_FIELD, TimeUtils.getStartOfDayDate(date).getTime())
                            .whereLessThanOrEqualTo(Session.DATE_FIELD, TimeUtils.getEndOfDayDate(date).getTime())
                            .whereArrayContains(Session.COACHES_EMAILS_FIELD, coachEmail)
                            .addSnapshotListener(((value, error) -> {
                                if (checkIsSnapshotInvalid(emitter, error)) {
                                    Log.d(AppConsts.DEBUG_TAG, "CoachDaysSessionsListDataListener: value null");
                                    return;
                                }

                                if (value == null) {
                                    EventBus.getDefault().post(new CoachDaysSessionsListDataListenerEvent(new ArrayList<>()));
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
