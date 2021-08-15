package com.example.fitnessfactory.data.events;

import org.greenrobot.eventbus.EventBus;

public class BaseEvent {

    public void removeStickyEvent() {
        BaseEvent stickyEvent = EventBus.getDefault().getStickyEvent(this.getClass());
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
    }
}
