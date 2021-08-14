package com.example.fitnessfactory.ui.viewmodels;

import android.app.Notification;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.utils.GuiUtils;

import java.util.HashMap;

import icepick.Icepick;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BaseViewModel extends ViewModel {

    private CompositeDisposable disposables = new CompositeDisposable();
    private HashMap<String, Object> handle = new HashMap<>();
    private static final String HANDLE = "HANDLE";
    private boolean loading = false;

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

    private void unsubscribe() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    protected void addSubscription(Disposable disposable) {
        disposables.add(disposable);
    }

    protected <T> void subscribeInIOThread(Completable subscriber,
                                           Action onComplete,
                                           Consumer<? super Throwable> onError) {
        addSubscription(subscriber
                .subscribeOn(getIOScheduler())
                .observeOn(getMainThreadScheduler())
                .subscribe(onComplete, onError));
    }

    protected <T> void subscribeInIOThread(Completable subscriber,
                                           Consumer<? super Throwable> onError) {
        addSubscription(subscriber
                .subscribeOn(getIOScheduler())
                .observeOn(getMainThreadScheduler())
                .subscribe(() -> {}, onError));
    }

    protected <T> void subscribeInIOThread(Completable subscriber) {
        addSubscription(subscriber
                .subscribeOn(getIOScheduler())
                .observeOn(getMainThreadScheduler())
                .subscribe());
    }

    protected <T> void subscribeInIOThread(Single<T> subscriber, SingleData<T> observer) {
        addSubscription(subscriber
                .subscribeOn(getIOScheduler())
                .observeOn(getMainThreadScheduler())
                .subscribe(observer.getObserver()::onSuccess, observer.getObserver()::onError));
    }

    protected <T> void subscribeInMainThread(Single<T> subscriber, SingleData<T> observer) {
        addSubscription(subscriber
                .subscribeOn(getMainThreadScheduler())
                .observeOn(getMainThreadScheduler())
                .subscribe(observer.getObserver()::onSuccess, observer.getObserver()::onError));
    }

    protected Scheduler getMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    protected Scheduler getIOScheduler() {
        return Schedulers.io();
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
