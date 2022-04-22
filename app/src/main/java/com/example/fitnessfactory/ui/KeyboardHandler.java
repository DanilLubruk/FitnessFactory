package com.example.fitnessfactory.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.ref.WeakReference;

public class KeyboardHandler {
    private WeakReference<Activity> activityRef;
    private boolean keyBoardVisible = false;
    private boolean rootViewExists = false;

    public void attach(Activity activity) {
        this. activityRef = new WeakReference<>(activity);
        View rootView = activity.findViewById(android.R.id.content);
        if (rootView != null) {
            rootViewExists = true;
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getRootView().getHeight();
                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;
                keyBoardVisible = keypadHeight > screenHeight * 0.15;
                Log.d("keyboard_state", "keyBoardVisible = " + keyBoardVisible);
            });
        }
        else {
            rootViewExists = false;
        }
    }

    public void detach() {
        activityRef.clear();
    }

    public void tryToHideKeyboard() {
        Activity activity = activityRef.get();
        if (activity == null) {
            return;
        }
        if (rootViewExists) {
            if (keyBoardVisible) {
                toggleSoftKeyboard();
            }
        }
        else {
            hideSoftKeyboard();
        }
    }

    public void tryToShowKeyBoard() {
        Activity activity = activityRef.get();
        if (activity == null) {
            return;
        }
        if (rootViewExists) {
            if (!keyBoardVisible) {
                toggleSoftKeyboard();
            }
        }
        else {
            showSoftKeyboard();
        }
    }

    private void hideSoftKeyboard() {
        Activity activity = activityRef.get();
        if (activity == null) {
            return;
        }
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    private void showSoftKeyboard() {
        Activity activity = activityRef.get();
        if (activity == null) {
            return;
        }
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void toggleSoftKeyboard() {
        Activity activity = activityRef.get();
        if (activity == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

}
