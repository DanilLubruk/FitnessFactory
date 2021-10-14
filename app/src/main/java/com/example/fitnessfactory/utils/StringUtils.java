package com.example.fitnessfactory.utils;

import android.text.TextUtils;

public class StringUtils {

    public static boolean isEmpty(CharSequence string) {
        return string != null && isEmpty(string.toString());
    }

    public static boolean isEmpty(String string) {
        return string != null && TextUtils.isEmpty(string.trim());
    }

    public static String getEmptyString () {
        return "";
    }
}
