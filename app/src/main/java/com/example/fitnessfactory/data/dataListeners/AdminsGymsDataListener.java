package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.events.AdminGymsListListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.OwnerAdminsCollection;
import com.example.fitnessfactory.data.managers.BaseManager;
import com.example.fitnessfactory.data.models.Admin;
import com.example.fitnessfactory.data.repositories.AdminsRepository;
import com.example.fitnessfactory.utils.RxUtils;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import io.reactivex.Single;

public class AdminsGymsDataListener extends BaseDataListener {

    @Inject
    AdminsRepository adminsRepository;

    private AtomicReference<ListenerRegistration> adminsGymsListener = new AtomicReference<>();

    @Override
    public String getRoot() {
        return OwnerAdminsCollection.getRoot();
    }

    public AdminsGymsDataListener() {
        FFApp.get().getAppComponent().inject(this);
    }

    public void setAdminsGymsListener(String adminEmail) {
        getAdminGymsListListener(adminEmail)
                .subscribeOn(getIOScheduler())
                .observeOn(getIOScheduler())
                .subscribe(adminsGymsListener::set, RxUtils::handleError)
                .dispose();
    }

    private Single<ListenerRegistration> getAdminGymsListListener(String adminEmail) {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getCollection().whereEqualTo(Admin.USER_EMAIL_FIELD, adminEmail)
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

    public void removeAdminsGymsListener() {
        if (adminsGymsListener.get() != null) {
            adminsGymsListener.get().remove();
        }
    }
}
