package com.example.fitnessfactory;

import com.example.fitnessfactory.di.AppComponent;
import com.example.fitnessfactory.di.AppScope;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {TestAppModule.class})
@AppScope
@Singleton
public interface TestAppComponent extends AppComponent {
}
