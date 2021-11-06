package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.ui.viewmodels.DataOperatingViewModel;

public abstract class ListViewModel<ItemType> extends DataOperatingViewModel implements DataListListener<ItemType> {

    public MutableLiveData<Boolean> doInterruptProgress = new MutableLiveData<>();
}
