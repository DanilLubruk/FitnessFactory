package com.example.fitnessfactory.ui.viewmodels.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.ui.viewmodels.AuthViewModel;
import com.example.fitnessfactory.ui.viewmodels.SplashActivityViewModel;

import javax.inject.Inject;

public class SplashActivityViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    SplashActivityViewModel splashActivityViewModel;

    public SplashActivityViewModelFactory() {
        FFApp.get().getAppComponent().inject(this);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) splashActivityViewModel;
    }
}