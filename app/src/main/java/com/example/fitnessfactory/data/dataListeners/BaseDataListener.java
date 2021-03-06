package com.example.fitnessfactory.data.dataListeners;

import android.util.Log;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.firestoreCollections.CollectionOperator;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.system.SafeReference;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.RxErrorsHandler;
import com.example.fitnessfactory.utils.RxUtils;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import io.reactivex.CompletableEmitter;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseDataListener extends CollectionOperator {

    private final CompositeDisposable disposables = new CompositeDisposable();
    protected final SafeReference<ListenerRegistration> dataListener = new SafeReference<>();
    private RxErrorsHandler rxErrorsHandler = new RxUtils();

    protected FirebaseFirestore getFirestore() {
        return FirebaseFirestore.getInstance();
    }

    protected Scheduler getMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    protected Scheduler getIOScheduler() {
        return Schedulers.io();
    }

    private void unsubscribe() {
        if (!disposables.isDisposed()) {
            disposables.clear();
        }
    }

    protected void addSubscription(Disposable disposable) {
        disposables.add(disposable);
    }

    protected void setListenerRegistration(Single<ListenerRegistration> dataGetter) {
        subscribeInIOThread(
                dataGetter,
                new SingleData<>(dataListener::set, getErrorHandler()::handleError));
    }

    private <T> void subscribeInIOThread(Single<T> subscriber, SingleData<T> observer) {
        addSubscription(subscriber
                .subscribeOn(getIOScheduler())
                .observeOn(getMainThreadScheduler())
                .subscribe(observer.getObserver()::onSuccess, observer.getObserver()::onError));
    }

    protected <T> void reportError(SingleEmitter<T> emitter, Exception error) {
        if (!emitter.isDisposed()) {
            emitter.onError(error);
        }
    }

    protected void reportError(CompletableEmitter emitter, Exception error) {
        if (!emitter.isDisposed()) {
            emitter.onError(error);
        }
    }

    protected <T> boolean checkIsSnapshotInvalid(SingleEmitter<T> emitter,
                                                 QuerySnapshot value,
                                                 FirebaseFirestoreException error) {
        if (error != null) {
            reportError(emitter, error);
            return true;
        }
        if (value == null) {
            reportError(emitter, new Exception(ResUtils.getString(R.string.message_error_data_obtain)));
            return true;
        }

        return false;
    }

    protected <T> boolean checkIsSnapshotInvalid(SingleEmitter<T> emitter,
                                                 FirebaseFirestoreException error) {
        if (error != null) {
            reportError(emitter, error);
            return true;
        }

        return false;
    }

    public void setRxErrorsHandler(RxErrorsHandler rxErrorsHandler) {
        this.rxErrorsHandler = rxErrorsHandler;
    }

    protected RxErrorsHandler getErrorHandler() {
        return rxErrorsHandler;
    }

    public void stopDataListener() {
        if (dataListener.get() != null) {
            dataListener.get().remove();
        }
        unsubscribe();
    }
}
