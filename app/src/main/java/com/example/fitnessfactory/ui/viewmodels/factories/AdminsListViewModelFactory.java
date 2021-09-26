package com.example.fitnessfactory.ui.viewmodels.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.dataListeners.AdminsListDataListener;
import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.ui.viewmodels.lists.AdminListViewModel;

import javax.inject.Inject;

public class AdminsListViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    AdminsAccessManager accessManager;
    @Inject
    AdminsDataManager dataManager;
    @Inject
    AdminsListDataListener dataListener;

    public AdminsListViewModelFactory() {
        FFApp.get().getAppComponent().inject(this);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AdminListViewModel(accessManager, dataManager, dataListener);
    }
}
