package com.example.fitnessfactory.ui.viewmodels.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList.AdminGymsListTabViewModel;

import javax.inject.Inject;

public class AdminGymsListTabViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    AdminGymsListTabViewModel viewModel;

    public AdminGymsListTabViewModelFactory() {
        FFApp.get().getAppComponent().inject(this);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) viewModel;
    }
}
