package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.data.firestoreCollections.CollectionOperator;
import com.example.fitnessfactory.system.SafeReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import io.reactivex.CompletableEmitter;
import io.reactivex.Scheduler;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseDataListener extends CollectionOperator {

    private final CompositeDisposable disposables = new CompositeDisposable();
    protected final SafeReference<ListenerRegistration> dataListener = new SafeReference<>();

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

    public void stopDataListener() {
        if (dataListener.get() != null) {
            dataListener.get().remove();
        }
        unsubscribe();
    }
}
