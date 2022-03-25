package com.example.fitnessfactory.data.dataListeners;

public interface DataListener {

    default void startDataListener(String value) { }

    default void startDataListener() { }

    void stopDataListener();
}
