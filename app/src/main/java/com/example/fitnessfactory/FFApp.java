package com.example.fitnessfactory;

import android.app.Application;

public class FFApp extends Application {

    private static FFApp instance;

    public static FFApp get() {
        return instance;
    }

    private void setInstance(FFApp instance) {
        FFApp.instance = instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
    }
}
