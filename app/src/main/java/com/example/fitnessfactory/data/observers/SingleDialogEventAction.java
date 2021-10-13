package com.example.fitnessfactory.data.observers;

import com.example.fitnessfactory.ui.activities.BaseActivity;

import io.reactivex.Single;

public interface SingleDialogEventAction<EmittedType, ArgType> {

     Single<EmittedType> showDialog(BaseActivity context, ArgType argument);
}
