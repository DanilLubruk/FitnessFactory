package com.example.fitnessfactory.utils;

import com.example.fitnessfactory.FFApp;

public class ResUtils {

    public static int getColor(int id) {
        return FFApp.get().getResources().getColor(id);
    }

    public static int getDimen(int id) {
        return (int) FFApp.get().getResources().getDimension(id);
    }

    public static String getString(int id) {
        return FFApp.get().getResources().getString(id);
    }
}
