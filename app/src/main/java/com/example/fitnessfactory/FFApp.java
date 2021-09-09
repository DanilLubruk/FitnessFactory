package com.example.fitnessfactory;

import android.app.Application;

import com.example.fitnessfactory.di.AppComponent;
import com.example.fitnessfactory.di.AppModule;
import com.example.fitnessfactory.di.DaggerAppComponent;

public class FFApp extends Application {

    private static FFApp instance;
    private AppComponent appComponent;

    public static FFApp get() {
        return instance;
    }

    private AppModule getAppModule() {
        return new AppModule();
    }

    private void setInstance(FFApp instance) {
        FFApp.instance = instance;
    }

    public AppComponent getAppComponent() {
        if (appComponent == null) {
            initAppComponent();
        }

        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        initAppComponent();
    }

    protected void initAppComponent() {
       appComponent = DaggerAppComponent
                .builder()
                .appModule(getAppModule())
                .build();
    }
}
