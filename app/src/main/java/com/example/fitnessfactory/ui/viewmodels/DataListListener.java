package com.example.fitnessfactory.ui.viewmodels;

public interface DataListListener<ItemType> {

    void startDataListener();

    void stopDataListener();

    void deleteItem(ItemType item);
}
