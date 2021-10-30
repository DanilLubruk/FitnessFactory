package com.example.fitnessfactory.ui.viewmodels;

import com.example.fitnessfactory.data.dataListeners.DataListener;

public interface DataListListener<ItemType> extends DataListener {

    void deleteItem(ItemType item);
}
