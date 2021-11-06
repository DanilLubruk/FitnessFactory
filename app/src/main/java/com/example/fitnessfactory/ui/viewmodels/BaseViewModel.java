package com.example.fitnessfactory.ui.viewmodels;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.callbacks.NullableCallback;
import com.example.fitnessfactory.data.managers.RxManager;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.RxErrorsHandler;

import java.util.HashMap;

import icepick.Icepick;
import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public abstract class BaseViewModel extends ViewModel {

    private HashMap<String, Object> handle = new HashMap<>();
    private static final String HANDLE = "HANDLE";
    private boolean loading = false;
    private final RxManager rxManager = new RxManager();

    @Override
    protected void onCleared() {
        super.onCleared();
        unsubscribe();
        Log.d("base_model", "view cleared unsubscribe");
    }

    public boolean isLoading() {
        return loading;
    }

    protected void startLoading() {
        this.loading = true;
    }

    protected void stopLoading() {
        this.loading = false;
    }

    protected HashMap<String, Object> getHandle() {
        return handle;
    }

    protected boolean hasHandle() {
        return handle.size() > 0;
    }

    protected void unsubscribe() {
        rxManager.unsubscribe();
    }

    public Scheduler getMainThreadScheduler() {
        return rxManager.getMainThreadScheduler();
    }

    public Scheduler getIOScheduler() {
        return rxManager.getIOScheduler();
    }

    public void setMainThreadScheduler(Scheduler mainScheduler) {
        rxManager.setMainThreadScheduler(mainScheduler);
    }

    public void setIOScheduler(Scheduler ioScheduler) {
        rxManager.setIOScheduler(ioScheduler);
    }

    public void setRxErrorsHandler(RxErrorsHandler rxErrorsHandler) {
        rxManager.setRxErrorsHandler(rxErrorsHandler);
    }

    public <T> void subscribeInIOThread(Completable subscriber,
                                        Action onComplete,
                                        Consumer<? super Throwable> onError) {
        rxManager.subscribeInIOThread(subscriber, onComplete, onError);
    }

    public <T> void subscribeInIOThread(Single<T> subscriber, SingleData<T> observer) {
        rxManager.subscribeInIOThread(subscriber, observer);
    }

    public <T> void subscribeInMainThread(Single<T> subscriber, SingleData<T> observer) {
        rxManager.subscribeInMainThread(subscriber, observer);
    }

    public <T> void subscribe(Single<T> subscriber, SingleData<T> observer) {
        rxManager.subscribe(subscriber, observer);
    }

    public <T> void subscribe(Completable subscriber,
                              Action onComplete,
                              Consumer<? super Throwable> onError) {
        rxManager.subscribe(subscriber, onComplete, onError);
    }

    public <T> void subscribeInIOThread(Completable subscriber) {
        rxManager.subscribeInIOThread(subscriber, getErrorHandler()::handleError);
    }

    public void addSubscription(Disposable disposable) {
        rxManager.addSubscription(disposable);
    }

    public RxErrorsHandler getErrorHandler() {
        return rxManager.getErrorHandler();
    }

    protected void handleNullPointerException(NullableCallback callback, String message) {
        getErrorHandler().handleNullPointerException(callback, message);
    }

    public void saveState(Bundle outState) {
        if (outState != null) {
            Icepick.saveInstanceState(this, outState);
            outState.putSerializable(HANDLE, handle);
        }
    }

    public void restoreState(Bundle inState) {
        if (inState != null) {
            Icepick.restoreInstanceState(this, inState);
            handle = (HashMap<String, Object>) inState.getSerializable(HANDLE);
        }
    }
}
