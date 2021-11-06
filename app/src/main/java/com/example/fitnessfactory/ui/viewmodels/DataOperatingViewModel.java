package com.example.fitnessfactory.ui.viewmodels;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;

public abstract class DataOperatingViewModel extends BaseViewModel {

    protected SingleLiveEvent<Boolean> handleItemSavingNullError(SingleLiveEvent<Boolean> observer) {
        return handleItemNullError(
                observer,
                getErrorSavingMessage().concat(getItemNullClause()));
    }

    protected void handleItemDeletingNullError() {
        handleItemDeletingNullError(new SingleLiveEvent<>());
    }

    protected SingleLiveEvent<Boolean> handleItemDeletingNullError(SingleLiveEvent<Boolean> observer) {
        return handleItemNullError(
                observer,
                getErrorDeletingMessage().concat(getItemNullClause()));
    }

    protected void handleItemObtainingNullError() {
        GuiUtils.showMessage(getErrorObtainingMessage().concat(getItemNullClause()));
    }

    protected void handleItemOperationError() {
        GuiUtils.showMessage(getErrorOperationMessage().concat(getItemNullClause()));
    }

    private SingleLiveEvent<Boolean> handleItemNullError(SingleLiveEvent<Boolean> observer, String message) {
        observer.setValue(false);
        GuiUtils.showMessage(message);

        return observer;
    }

    protected String getErrorSavingMessage() {
        return ResUtils.getString(R.string.message_error_data_save);
    }

    protected String getErrorDeletingMessage() {
        return ResUtils.getString(R.string.message_error_data_delete);
    }

    protected String getErrorObtainingMessage() {
        return ResUtils.getString(R.string.message_error_data_obtain);
    }

    protected String getErrorOperationMessage() {
        return ResUtils.getString(R.string.message_error_data_operate);
    }

    protected abstract String getItemNullClause();

    protected String getErrorMessageBreak() {
        return " - ";
    }
}
