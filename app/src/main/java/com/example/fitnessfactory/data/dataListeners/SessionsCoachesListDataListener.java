package com.example.fitnessfactory.data.dataListeners;

import android.util.Log;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.SessionsCoachesListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.OwnerCoachesCollection;
import com.example.fitnessfactory.data.models.Personnel;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class SessionsCoachesListDataListener extends BaseDataListener implements ArgDataListener<List<String>> {

    @Override
    protected String getRoot() {
        return OwnerCoachesCollection.getRoot();
    }

    @Override
    public void startDataListener(List<String> coachesIds) {
        if (coachesIds == null || coachesIds.size() == 0) {
            return;
        }

        setListenerRegistration(getDataListener(coachesIds));
    }

    private Single<ListenerRegistration> getDataListener(List<String> coachesIds) {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration = getCollection()
                    .whereIn(Personnel.ID_FIELD, coachesIds)
                    .addSnapshotListener((value, error) -> {
                        if (checkIsSnapshotInvalid(emitter, value, error)) {
                            Log.d(AppConsts.DEBUG_TAG, "SessionsCoachesListDataListener: value null");
                            return;
                        }

                        List<Personnel> coaches = value.toObjects(Personnel.class);

                        List<String> coachesEmails = new ArrayList<>();
                        for (Personnel coach : coaches) {
                            coachesEmails.add(coach.getUserEmail());
                        }

                        EventBus.getDefault().post(new SessionsCoachesListDataListenerEvent(coachesEmails));
                    });

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }
}
