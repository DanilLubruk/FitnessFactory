package com.example.fitnessfactory.utils;

import android.text.TextUtils;

public class StringUtils {

    public static boolean isEmpty(CharSequence string) {
        if (string == null) {
            return true;
        }

        return isEmpty(string.toString());
    }

    public static boolean isEmpty(String string) {
        if (string == null) {
            return true;
        }

        return TextUtils.isEmpty(string.trim());
    }
}
