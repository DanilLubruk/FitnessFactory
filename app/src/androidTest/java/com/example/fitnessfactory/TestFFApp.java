package com.example.fitnessfactory;

public class TestFFApp extends FFApp {

    private TestAppComponent testAppComponent;

    @Override
    public TestAppComponent getAppComponent() {
        if (testAppComponent == null) {

        }
        return testAppComponent;
    }

    public static TestFFApp get() {
        return (TestFFApp) FFApp.get();
    }

    @Override
    public void initAppComponent() {
        super.initAppComponent();

    }
}
