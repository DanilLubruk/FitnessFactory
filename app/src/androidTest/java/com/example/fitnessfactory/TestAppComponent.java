package com.example.fitnessfactory;
import com.example.fitnessfactory.di.AppScope;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {TestAppModule.class})
@AppScope
@Singleton
public interface TestAppComponent {

    void inject(AdminsAccessManagerTests adminsAccessManagerTests);
}
