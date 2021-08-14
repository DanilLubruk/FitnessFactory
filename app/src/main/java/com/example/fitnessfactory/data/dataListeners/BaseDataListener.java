package com.example.fitnessfactory.data.dataListeners;

import com.example.fitnessfactory.data.firestoreCollections.CollectionOperator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.CompletableEmitter;
import io.reactivex.Scheduler;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseDataListener implements CollectionOperator {

    private CollectionReference colReference;

    public CollectionReference getCollection() {
        initCollection();
        return colReference;
    }

    protected FirebaseFirestore getFirestore() {
        return FirebaseFirestore.getInstance();
    }

    private void initCollection() {
        colReference = FirebaseFirestore.getInstance().collection(getRoot());
    }

    protected Scheduler getMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    protected Scheduler getIOScheduler() {
        return Schedulers.io();
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
}
