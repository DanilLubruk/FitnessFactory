package com.example.fitnessfactory;

import com.example.fitnessfactory.di.AppComponent;
import com.example.fitnessfactory.di.AppScope;

import dagger.Component;

@Component(modules = {TestAppModule.class})
@AppScope
public interface TestAppComponent extends AppComponent {
}
