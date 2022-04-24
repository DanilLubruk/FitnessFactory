package com.example.fitnessfactory.data.dataListeners;

import android.util.Log;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.CoachDaysSessionsListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.SessionsCollection;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
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

    public void startDataListener(Date date, String coachUserId) {
        setListenerRegistration(getDataListener(date, coachUserId));
    }

    private Single<ListenerRegistration> getDataListener(Date date, String coachUserId) {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    SessionsRepository.getDayEqualsQuery(getCollection(), date)
                            .whereArrayContains(Session.COACHES_IDS_FIELD, coachUserId)
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
