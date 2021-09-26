package com.example.fitnessfactory.ui.viewmodels.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.dataListeners.CoachesListDataListener;
import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.ui.viewmodels.lists.CoachesListViewModel;

import javax.inject.Inject;

public class CoachesListViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    CoachesAccessManager accessManager;
    @Inject
    CoachesDataManager dataManager;
    @Inject
    CoachesListDataListener dataListener;

    public CoachesListViewModelFactory() {
        FFApp.get().getAppComponent().inject(this);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CoachesListViewModel(accessManager, dataManager, dataListener);
    }
}
