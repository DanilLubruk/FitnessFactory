package com.example.fitnessfactory.ui.viewmodels.editors;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;

public abstract class EditorViewModel extends BaseViewModel {

    public abstract SingleLiveEvent<Boolean> isModified();

    public abstract SingleLiveEvent<Boolean> save();

    public abstract SingleLiveEvent<Boolean> delete();

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

    private SingleLiveEvent<Boolean> handleItemNullError(SingleLiveEvent<Boolean> observer, String message) {
        observer.setValue(false);
        GuiUtils.showMessage(message);

        return observer;
    }

    private String getErrorSavingMessage() {
        return ResUtils.getString(R.string.message_error_data_save);
    }

    private String getErrorDeletingMessage() {
        return ResUtils.getString(R.string.message_error_data_delete);
    }

    private String getErrorObtainingMessage() {
        return ResUtils.getString(R.string.message_error_data_obtain);
    }

    protected abstract String getItemNullClause();

    protected String getErrorMessageBreak() {
        return " - ";
    }
}
