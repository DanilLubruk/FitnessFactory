package com.example.fitnessfactory.data.managers;

import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.utils.RxErrorsHandler;
import com.example.fitnessfactory.utils.RxUtils;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxManager {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private RxErrorsHandler rxErrorsHandler = new RxUtils();
    private Scheduler ioScheduler = Schedulers.io();
    private Scheduler mainScheduler = AndroidSchedulers.mainThread();

    public void addSubscription(Disposable disposable) {
        disposables.add(disposable);
    }

    public <T> void subscribe(Single<T> subscriber, SingleData<T> observer) {
        addSubscription(subscriber
                .subscribe(observer.getObserver()::onSuccess, observer.getObserver()::onError));
    }

    public <T> void subscribeInIOThread(Completable subscriber,
                                        Action onComplete,
                                        Consumer<? super Throwable> onError) {
        addSubscription(subscriber
                .subscribeOn(getIOScheduler())
                .observeOn(getMainThreadScheduler())
                .subscribe(onComplete, onError));
    }

    public <T> void subscribe(Completable subscriber,
                              Action onComplete,
                              Consumer<? super Throwable> onError) {
        addSubscription(subscriber
                .subscribe(onComplete, onError));
    }

    public <T> void subscribeInIOThread(Completable subscriber,
                                        Consumer<? super Throwable> onError) {
        addSubscription(subscriber
                .subscribeOn(getIOScheduler())
                .observeOn(getMainThreadScheduler())
                .subscribe(() -> {
                }, onError));
    }

    public <T> void subscribeInIOThread(Completable subscriber) {
        addSubscription(subscriber
                .subscribeOn(getIOScheduler())
                .observeOn(getMainThreadScheduler())
                .subscribe());
    }

    public <T> void subscribeInIOThread(Single<T> subscriber, SingleData<T> observer) {
        addSubscription(subscriber
                .subscribeOn(getIOScheduler())
                .observeOn(getMainThreadScheduler())
                .subscribe(observer.getObserver()::onSuccess, observer.getObserver()::onError));
    }

    public <T> void subscribeInMainThread(Single<T> subscriber, SingleData<T> observer) {
        addSubscription(subscriber
                .subscribeOn(getMainThreadScheduler())
                .observeOn(getMainThreadScheduler())
                .subscribe(observer.getObserver()::onSuccess, observer.getObserver()::onError));
    }

    public Scheduler getMainThreadScheduler() {
        return mainScheduler;
    }

    public void setMainThreadScheduler(Scheduler mainScheduler) {
        this.mainScheduler = mainScheduler;
    }

    public Scheduler getIOScheduler() {
        return ioScheduler;
    }

    public void setIOScheduler(Scheduler ioScheduler) {
        this.ioScheduler = ioScheduler;
    }

    public void setRxErrorsHandler(RxErrorsHandler rxErrorsHandler) {
        this.rxErrorsHandler = rxErrorsHandler;
    }

    public RxErrorsHandler getErrorHandler() {
        return rxErrorsHandler;
    }

    public void unsubscribe() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}
