package com.example.fitnessfactory.ui.viewmodels;

import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;

public abstract class EditorViewModel extends BaseViewModel {

    public abstract SingleLiveEvent<Boolean> isModified();

    public abstract SingleLiveEvent<Boolean> save();

    public abstract SingleLiveEvent<Boolean> delete();
}
