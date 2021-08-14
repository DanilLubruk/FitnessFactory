package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.events.AdminsListDataListenerEvent;
import com.example.fitnessfactory.data.models.AdminAccessEntry;
import com.example.fitnessfactory.data.repositories.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.AdminsRepository;
import com.example.fitnessfactory.utils.RxUtils;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import io.reactivex.Single;

public class AdminsListDataListener extends BaseDataListener {

    @Inject
    AdminsRepository adminsRepository;
    @Inject
    AdminsAccessRepository adminsAccessRepository;

    public AdminsListDataListener() {
        FFApp.get().getAppComponent().inject(this);
    }

    public void setAdminsListListener() {
        adminsRepository.getAdminsEmailsAsync()
                .subscribeOn(getIOScheduler())
                .observeOn(getIOScheduler())
                .flatMap(this::getAdminsListListener)
                .subscribe(
                        dataListener::set,
                        RxUtils::handleError)
                .dispose();
    }

    private Single<ListenerRegistration> getAdminsListListener(List<String> adminsEmails) {
        return Single.create(emitter -> {
            ListenerRegistration adminsListListener =
                    adminsAccessRepository
                            .getAdminsListQuery(adminsEmails)
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
}
