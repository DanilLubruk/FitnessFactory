package com.example.fitnessfactory.data.observers;

import com.example.fitnessfactory.ui.activities.BaseActivity;

import io.reactivex.Single;

public class SingleDialogEvent<EmittedType, ArgType> {

    private BaseActivity context;
    private SingleDialogEventAction<EmittedType, ArgType> action;

    public SingleDialogEvent(BaseActivity context, SingleDialogEventAction<EmittedType, ArgType> action) {
        this.context = context;
        this.action = action;
    }

    public Single<EmittedType> showDialog(ArgType argument) {
        return action.showDialog(context, argument);
    }
}
