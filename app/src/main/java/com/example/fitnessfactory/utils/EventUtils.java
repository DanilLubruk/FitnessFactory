package com.example.fitnessfactory.utils;

import com.example.fitnessfactory.data.events.StickyEvent;

import org.greenrobot.eventbus.EventBus;

public class EventUtils {

    public static <T extends StickyEvent> void removeStickyEvent(Class<T> eventClass) {
        StickyEvent stickyEvent = EventBus.getDefault().getStickyEvent(eventClass);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
    }
}
