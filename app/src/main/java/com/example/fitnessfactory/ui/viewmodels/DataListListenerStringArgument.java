package com.example.fitnessfactory.ui.viewmodels;

import com.example.fitnessfactory.data.dataListeners.ArgDataListener;

public interface DataListListenerStringArgument<ItemType> extends ArgDataListener {

    void deleteItem(ItemType item);
}
