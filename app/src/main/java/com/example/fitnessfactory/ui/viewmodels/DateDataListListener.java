package com.example.fitnessfactory.ui.viewmodels;

import java.util.Date;

public interface DateDataListListener<ItemType> {

    void startDataListener(Date startDate);

    void stopDataListener();

    void deleteItem(ItemType item);
}
