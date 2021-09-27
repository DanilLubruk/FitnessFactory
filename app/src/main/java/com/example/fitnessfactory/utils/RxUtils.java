package com.example.fitnessfactory.utils;

import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.utils.dialogs.exceptions.DialogCancelledException;

public class RxUtils implements RxErrorsHandler {

    public void handleError(Throwable throwable) {
        throwable.printStackTrace();
        if (!(throwable instanceof DialogCancelledException)) {
            GuiUtils.showMessage(throwable.getLocalizedMessage());
        }
    }

    public void handleError(SingleLiveEvent<Boolean> observer, Throwable throwable) {
        observer.setValue(false);
        throwable.printStackTrace();
        if (!(throwable instanceof DialogCancelledException)) {
            GuiUtils.showMessage(throwable.getLocalizedMessage());
        }
    }
}
