package com.example.fitnessfactory.utils;

import android.text.TextUtils;

public class StringUtils {

    public static boolean isEmpty(String string) {
        return TextUtils.isEmpty(string.trim());
    }

    public static String getEmptyString () {
        return "";
    }
}
