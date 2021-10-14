package com.example.fitnessfactory.ui.viewmodels.editors;

import androidx.databinding.ObservableField;

import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.utils.GuiUtils;

public abstract class EditorViewModel extends BaseViewModel {

    public abstract SingleLiveEvent<Boolean> isModified();

    public abstract SingleLiveEvent<Boolean> save();

    public abstract SingleLiveEvent<Boolean> delete();

    protected SingleLiveEvent<Boolean> handleItemNullError(SingleLiveEvent<Boolean> observer) {
        observer.setValue(false);
        GuiUtils.showMessage(getItemNullMessage());

        return observer;
    }

    protected abstract String getItemNullMessage();
}
