package com.example.fitnessfactory;

import android.app.Application;
import android.util.Log;

import com.example.fitnessfactory.di.AppComponent;
import com.example.fitnessfactory.di.AppModule;
import com.example.fitnessfactory.di.DaggerAppComponent;

import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;

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
        RxJavaPlugins.setErrorHandler(e -> {
                    if (e instanceof UndeliverableException) {
                        e.printStackTrace();
                    } else if (e instanceof InterruptedException) {
                        e.printStackTrace();
                    } else {
                        Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                    }
                }
        );
    }

    public void initAppComponent() {
        appComponent = DaggerAppComponent
                .builder()
                .appModule(getAppModule())
                .build();
    }

    public void initSessionEditorComponent() {

    }
}
