package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.events.GymsListDataListenerEvent;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.GymRepository;
import com.example.fitnessfactory.utils.RxUtils;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class GymsListDataListener extends BaseDataListener {

    @Inject
    GymRepository gymRepository;

    public GymsListDataListener() {
        FFApp.get().getAppComponent().inject(this);
    }

    public void setGymsListListener() {
        getGymsListListener()
                .subscribeOn(getIOScheduler())
                .observeOn(getIOScheduler())
                .subscribe(dataListener::set, RxUtils::handleError)
                .dispose();
    }

    private Single<ListenerRegistration> getGymsListListener() {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    gymRepository
                            .getGymsListQuery()
                            .addSnapshotListener(((value, error) -> {
                                if (error != null) {
                                    reportError(emitter, error);
                                }

                                if (value != null) {
                                    List<Gym> gyms = value.toObjects(Gym.class);
                                    EventBus.getDefault().post(new GymsListDataListenerEvent(gyms));
                                }
                            }));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }
}
