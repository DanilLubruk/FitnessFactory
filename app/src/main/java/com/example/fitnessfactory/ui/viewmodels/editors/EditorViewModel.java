package com.example.fitnessfactory.ui.viewmodels.editors;

import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;

public abstract class EditorViewModel extends BaseViewModel {

    public abstract SingleLiveEvent<Boolean> isModified();

    public abstract SingleLiveEvent<Boolean> save();

    public abstract SingleLiveEvent<Boolean> delete();
}
