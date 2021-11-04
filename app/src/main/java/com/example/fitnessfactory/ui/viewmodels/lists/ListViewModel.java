package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;

public abstract class ListViewModel<ItemType> extends BaseViewModel implements DataListListener<ItemType> {

    public MutableLiveData<Boolean> doInterruptProgress = new MutableLiveData<>();
}
