package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.data.firestoreCollections.SessionsCollection;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Date;

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

        });
    }
}
