package com.example.fitnessfactory.data.observers;

import io.reactivex.functions.Consumer;
import io.reactivex.internal.observers.ConsumerSingleObserver;

public class SingleData<T> {

    private final ConsumerSingleObserver<T> observer;

    public SingleData(Consumer<? super T> onSuccess, Consumer<? super Throwable> onError) {
        observer = new ConsumerSingleObserver<>(onSuccess, onError);
    }

    public ConsumerSingleObserver<T> getObserver() {
        return observer;
    }
}
