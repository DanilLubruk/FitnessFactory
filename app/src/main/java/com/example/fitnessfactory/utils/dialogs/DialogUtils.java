package com.example.fitnessfactory.utils.dialogs;

import android.app.AlertDialog;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.ui.activities.BaseActivity;

import io.reactivex.Single;

public class DialogUtils {

    public static Single<Boolean> showAskDialog(BaseActivity context,
                                                String message,
                                                String okCaption,
                                                String cancelCaption) {
        return Single.create(emitter -> {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.caption_warning_title)
                    .setMessage(message)
                    .setPositiveButton(okCaption, ((dialog, which) -> {
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(true);
                        }
                    }))
                    .setNegativeButton(cancelCaption, ((dialog, which) -> {
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(false);
                        }
                    }))
                    .show();
        });
    }
}
