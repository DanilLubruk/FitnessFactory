package com.example.fitnessfactory.ui.viewmodels.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelList.ClientsListViewModel;

import javax.inject.Inject;

public class ClientsListViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    ClientsListViewModel viewModel;

    public ClientsListViewModelFactory() {
        FFApp.get().getAppComponent().inject(this);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) viewModel;
    }
}
