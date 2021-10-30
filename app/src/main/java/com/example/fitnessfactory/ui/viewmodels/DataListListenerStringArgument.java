package com.example.fitnessfactory.ui.viewmodels;

import com.example.fitnessfactory.data.dataListeners.DataListenerStringArgument;

public interface DataListListenerStringArgument<ItemType> extends DataListenerStringArgument {

    void deleteItem(ItemType item);
}
