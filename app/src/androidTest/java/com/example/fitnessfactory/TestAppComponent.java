package com.example.fitnessfactory;
import com.example.fitnessfactory.di.AppScope;
import com.example.fitnessfactory.managers.PersonnelAccessManagerTests;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {TestAppModule.class})
@AppScope
@Singleton
public interface TestAppComponent {

    void inject(PersonnelAccessManagerTests personnelAccessManagerTests);
}
