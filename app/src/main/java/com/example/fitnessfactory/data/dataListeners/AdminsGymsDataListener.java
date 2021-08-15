package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.data.events.AdminGymsListListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.OwnerAdminsCollection;
import com.example.fitnessfactory.data.models.Admin;
import com.example.fitnessfactory.utils.RxUtils;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import org.greenrobot.eventbus.EventBus;


import io.reactivex.Single;

public class AdminsGymsDataListener extends BaseDataListener {

    @Override
    protected String getRoot() {
        return OwnerAdminsCollection.getRoot();
    }

    public void setAdminsGymsListener(String adminEmail) {
        addSubscription(getAdminGymsListListener(adminEmail)
                .subscribeOn(getIOScheduler())
                .observeOn(getMainThreadScheduler())
                .subscribe(dataListener::set, RxUtils::handleError));
    }

    private Single<ListenerRegistration> getAdminGymsListListener(String adminEmail) {
        return Single.create(emitter -> {
            ListenerRegistration listenerRegistration =
                    getAdminQueryByEmail(adminEmail)
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

    private Query getAdminQueryByEmail(String adminEmail) {
        return getCollection().whereEqualTo(Admin.USER_EMAIL_FIELD, adminEmail);
    }
}
