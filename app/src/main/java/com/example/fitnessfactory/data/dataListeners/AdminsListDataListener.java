package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.data.events.AdminsListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.OwnerAdminsCollection;
import com.example.fitnessfactory.utils.RxUtils;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import org.greenrobot.eventbus.EventBus;
import io.reactivex.Single;

public class AdminsListDataListener extends BaseDataListener { ;

    @Override
    protected String getRoot() {
        return OwnerAdminsCollection.getRoot();
    }

    public void startAdminsListListener() {
        addSubscription(getAdminsListListener()
                .subscribeOn(getIOScheduler())
                .observeOn(getMainThreadScheduler())
                .subscribe(
                        dataListener::set,
                        RxUtils::handleError));
    }

    private Single<ListenerRegistration> getAdminsListListener() {
        return Single.create(emitter -> {
            ListenerRegistration adminsListListener =
                    getAdminsListQuery()
                            .addSnapshotListener(((value, error) -> {
                                if (error != null) {
                                    reportError(emitter, error);
                                }

                                EventBus.getDefault().post(new AdminsListDataListenerEvent());
                            }));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(adminsListListener);
            }
        });
    }

    private Query getAdminsListQuery() {
        return getCollection();
    }
}
