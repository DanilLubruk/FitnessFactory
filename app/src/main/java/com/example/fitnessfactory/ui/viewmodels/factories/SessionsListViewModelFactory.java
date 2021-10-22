package com.example.fitnessfactory.ui.viewmodels.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.ui.viewmodels.lists.SessionsListViewModel;

import javax.inject.Inject;

public class SessionsListViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    SessionsListViewModel viewModel;

    public SessionsListViewModelFactory() {
        FFApp.get().getAppComponent().inject(this);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) viewModel;
    }
}
