package com.example.fitnessfactory;

import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.utils.RxErrorsHandler;

public class TestRxUtils implements RxErrorsHandler {


    @Override
    public void handleError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void handleError(SingleLiveEvent<Boolean> observer, Throwable throwable) {
        observer.setValue(false);
        throwable.printStackTrace();
    }
}
