package com.example.fitnessfactory.ui.viewmodels;

import com.example.fitnessfactory.data.dataListeners.DataListener;

public interface DataListListener<ItemType> extends DataListener {

    default void deleteItem(ItemType item) { }

    default void deleteItem(String sessionId, ItemType item) { }
}
