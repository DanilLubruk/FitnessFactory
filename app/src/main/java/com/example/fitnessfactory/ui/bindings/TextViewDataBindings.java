package com.example.fitnessfactory.ui.bindings;

import android.text.TextUtils;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.example.fitnessfactory.utils.TimeUtils;

import java.util.Date;

public class TextViewDataBindings {

    @BindingAdapter("android:text")
    public static void setText(TextView view, Float value) {
        if (value == null)
            return;

        view.setText(String.valueOf(value));
    }

    @BindingAdapter("android:textDate")
    public static void setText(TextView view, Date value) {
        if (value == null) {
            return;
        }

        String formattedDateString = TimeUtils.dateToLocaleStr(value);
        if (!TextUtils.equals(view.getText(), formattedDateString)) {
            view.setText(formattedDateString);
        }
    }
}
