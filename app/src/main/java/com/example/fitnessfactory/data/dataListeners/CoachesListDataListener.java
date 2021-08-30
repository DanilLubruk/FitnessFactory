package com.example.fitnessfactory.data.dataListeners;

import android.util.Log;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.CoachesListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.OwnerCoachesCollection;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Single;

public class CoachesListDataListener extends BaseDataListener {

    @Override
    protected String getRoot() {
        return OwnerCoachesCollection.getRoot();
    }

    public void startDataListener() {
        setListenerRegistration(getDataListener());
    }

    private Single<ListenerRegistration> getDataListener() {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getCollection().addSnapshotListener((value, error) -> {
                        if (error != null) {
                            reportError(emitter, error);
                        }
                        if (value == null) {
                            Log.d(AppConsts.DEBUG_TAG, "CoachesListDataListener: value null");
                            reportError(emitter, new Exception(ResUtils.getString(R.string.message_error_data_obtain)));
                            return;
                        }

                        List<AppUser> coaches = value.toObjects(AppUser.class);
                        EventBus.getDefault().post(new CoachesListDataListenerEvent(coaches));
                    });

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }
}
