package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.data.events.AdminsListDataListenerEvent;
import com.example.fitnessfactory.data.firestoreCollections.OwnerAdminsCollection;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import org.greenrobot.eventbus.EventBus;
import io.reactivex.Single;

public class AdminsListDataListener extends BaseDataListener implements DataListener {

    @Override
    protected String getRoot() {
        return OwnerAdminsCollection.getRoot();
    }

    public void startDataListener() {
        setListenerRegistration(getDataListener());
    }

    private Single<ListenerRegistration> getDataListener() {
        return Single.create(emitter -> {
            ListenerRegistration adminsListListener =
                    getAdminsListQuery()
                            .addSnapshotListener(((value, error) -> {
                                if (checkIsSnapshotValid(emitter, error)) {
                                    return;
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
