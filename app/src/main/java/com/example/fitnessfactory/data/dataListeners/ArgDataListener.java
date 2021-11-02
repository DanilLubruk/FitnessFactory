package com.example.fitnessfactory.data.dataListeners;

public interface ArgDataListener<ArgType> {

    void startDataListener(ArgType argument);

    void stopDataListener();
}
