package com.example.fitnessfactory.utils;

import com.example.fitnessfactory.data.observers.SingleLiveEvent;

public class RxUtils {

    public static void handleError(Throwable throwable) {
        throwable.printStackTrace();
        GuiUtils.showMessage(throwable.getLocalizedMessage());
    }

    public static void handleError(SingleLiveEvent<Boolean> observer, Throwable throwable) {
        observer.setValue(false);
        throwable.printStackTrace();
        GuiUtils.showMessage(throwable.getLocalizedMessage());
    }
}
