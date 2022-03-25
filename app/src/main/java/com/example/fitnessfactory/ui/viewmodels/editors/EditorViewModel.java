package com.example.fitnessfactory.ui.viewmodels.editors;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataOperatingViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;

public abstract class EditorViewModel extends DataOperatingViewModel {

    public abstract SingleLiveEvent<Boolean> isModified();

    public abstract SingleLiveEvent<Boolean> save();

    public abstract SingleLiveEvent<Boolean> delete();
}
