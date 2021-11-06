package com.example.fitnessfactory.ui.activities.editors;

import com.example.fitnessfactory.data.events.StickyEvent;
import com.example.fitnessfactory.utils.EventUtils;

public abstract class TabParentEditorActivity<EventType extends StickyEvent> extends EditorActivity {

    protected abstract Class<EventType> getEventType();

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventUtils.removeStickyEvent(getEventType());
    }
}
