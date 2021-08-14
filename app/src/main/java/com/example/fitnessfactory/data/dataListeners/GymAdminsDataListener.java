package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.events.GymAdminsListListenerEvent;
import com.example.fitnessfactory.data.repositories.AdminsRepository;
import com.example.fitnessfactory.utils.RxUtils;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.Single;

public class GymAdminsDataListener extends BaseDataListener {

    @Inject
    AdminsRepository adminsRepository;

    public GymAdminsDataListener() {
        FFApp.get().getAppComponent().inject(this);
    }

    public void setGymAdminsDataListener(String gymId) {
        getGymAdminsListener(gymId)
                .subscribeOn(getIOScheduler())
                .observeOn(getIOScheduler())
                .subscribe(dataListener::set, RxUtils::handleError)
                .dispose();
    }

    private Single<ListenerRegistration> getGymAdminsListener(String gymId) {
        return Single.create(emitter -> {
           ListenerRegistration listenerRegistration =
                   adminsRepository
                           .getAdminQueryByGymId(gymId)
                           .addSnapshotListener(((value, error) -> {
                               if (error != null) {
                                   reportError(emitter, error);
                               }

                               EventBus.getDefault().post(new GymAdminsListListenerEvent());
                           }));

           if (!emitter.isDisposed()) {
               emitter.onSuccess(listenerRegistration);
           }
        });
    }
}
