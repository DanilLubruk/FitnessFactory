package com.example.fitnessfactory.utils;

import com.example.fitnessfactory.data.callbacks.NullableCallback;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;

public interface RxErrorsHandler {

    void handleError(Throwable throwable);

    void handleError(SingleLiveEvent<Boolean> observer, Throwable throwable);

    void handleNullPointerException(NullableCallback callback, String errorMessage);
}
