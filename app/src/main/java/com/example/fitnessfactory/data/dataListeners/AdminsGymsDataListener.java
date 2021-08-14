package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.events.AdminGymsListListenerEvent;
import com.example.fitnessfactory.data.repositories.AdminsRepository;
import com.example.fitnessfactory.utils.RxUtils;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.Single;

public class AdminsGymsDataListener extends BaseDataListener {

    @Inject
    AdminsRepository adminsRepository;

    public AdminsGymsDataListener() {
        FFApp.get().getAppComponent().inject(this);
    }

    public void setAdminsGymsListener(String adminEmail) {
        getAdminGymsListListener(adminEmail)
                .subscribeOn(getIOScheduler())
                .observeOn(getIOScheduler())
                .subscribe(dataListener::set, RxUtils::handleError)
                .dispose();
    }

    private Single<ListenerRegistration> getAdminGymsListListener(String adminEmail) {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    adminsRepository.getAdminQueryByEmail(adminEmail)
                            .addSnapshotListener(((value, error) -> {
                                if (error != null) {
                                    reportError(emitter, error);
                                }

                                EventBus.getDefault().post(new AdminGymsListListenerEvent());
                            }));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(listenerRegistration);
            }
        });
    }
}
