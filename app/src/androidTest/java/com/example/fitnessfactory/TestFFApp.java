package com.example.fitnessfactory;

import android.app.Application;

public class TestFFApp extends Application {

    private static TestAppComponent testAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static TestAppComponent getTestAppComponent() {
        if (testAppComponent == null) {
            initAppComponent();
        }

        return testAppComponent;
    }

    private static void initAppComponent() {
        testAppComponent =
                DaggerTestAppComponent
                        .builder()
                        .testAppModule(new TestAppModule())
                        .build();
    }
}
