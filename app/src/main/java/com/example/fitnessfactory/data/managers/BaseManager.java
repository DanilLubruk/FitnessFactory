package com.example.fitnessfactory.data.managers;

import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.utils.GuiUtils;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BaseManager {

    private Disposable disposable;

    private void unsubscribe() {
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    protected void addSubscription(Disposable disposable) {
        this.disposable = disposable;
    }

    protected Scheduler getMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    protected Scheduler getIOScheduler() {
        return Schedulers.io();
    }

    protected void handleError(Throwable throwable) {
        throwable.printStackTrace();
        GuiUtils.showMessage(throwable.getLocalizedMessage());
    }

    protected void handleError(AtomicReference<Boolean> observer, Throwable throwable) {
        observer.set(false);
        throwable.printStackTrace();
        GuiUtils.showMessage(throwable.getLocalizedMessage());
    }
}
