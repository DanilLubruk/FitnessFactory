package com.example.fitnessfactory.utils;

import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.utils.dialogs.exceptions.DialogCancelledException;

public class RxUtils {

    public static void handleError(Throwable throwable) {
        throwable.printStackTrace();
        if (!(throwable instanceof DialogCancelledException)) {
            GuiUtils.showMessage(throwable.getLocalizedMessage());
        }
    }

    public static void handleError(SingleLiveEvent<Boolean> observer, Throwable throwable) {
        observer.setValue(false);
        throwable.printStackTrace();
        if (!(throwable instanceof DialogCancelledException)) {
            GuiUtils.showMessage(throwable.getLocalizedMessage());
        }
    }
}
