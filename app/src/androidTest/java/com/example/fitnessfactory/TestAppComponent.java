package com.example.fitnessfactory;
import com.example.fitnessfactory.di.AppScope;
import com.example.fitnessfactory.managers.access.AdminsAccessManagerTests;
import com.example.fitnessfactory.managers.access.CoachesAccessManagerTests;
import com.example.fitnessfactory.managers.access.PersonnelAccessManagerTests;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {TestAppModule.class})
@AppScope
@Singleton
public interface TestAppComponent {

    void inject(AdminsAccessManagerTests adminsAccessManagerTests);
    void inject(CoachesAccessManagerTests coachesAccessManagerTests);
}
