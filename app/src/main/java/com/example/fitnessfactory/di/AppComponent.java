package com.example.fitnessfactory.di;

import com.example.fitnessfactory.ui.viewmodels.AuthViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@AppScope
@Singleton
public interface AppComponent {

    void inject(AuthViewModel authViewModel);
}
