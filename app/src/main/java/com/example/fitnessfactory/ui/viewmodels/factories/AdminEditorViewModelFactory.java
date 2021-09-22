package com.example.fitnessfactory.ui.viewmodels.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.dataListeners.AdminGymsListDataListener;
import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.ui.viewmodels.AdminEditorViewModel;

import javax.inject.Inject;

public class AdminEditorViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    OwnerAdminsRepository ownerAdminsRepository;
    @Inject
    AdminsAccessManager adminsAccessManager;
    @Inject
    AdminsDataManager adminsDataManager;
    @Inject
    AdminGymsListDataListener adminGymsListDataListener;

    public AdminEditorViewModelFactory() {
        FFApp.get().getAppComponent().inject(this);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AdminEditorViewModel(
                ownerAdminsRepository,
                adminsAccessManager,
                adminsDataManager,
                adminGymsListDataListener);
    }
}
