package com.example.fitnessfactory.ui.viewmodels.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.dataListeners.CoachGymsListDataListener;
import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.ui.viewmodels.CoachEditorViewModel;

import javax.inject.Inject;

public class CoachEditorViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    OwnerCoachesRepository ownerCoachesRepository;
    @Inject
    CoachesAccessManager coachesAccessManager;
    @Inject
    CoachesDataManager coachesDataManager;
    @Inject
    CoachGymsListDataListener coachGymsListDataListener;

    public CoachEditorViewModelFactory() {
        FFApp.get().getAppComponent().inject(this);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CoachEditorViewModel(
                ownerCoachesRepository,
                coachesAccessManager,
                coachesDataManager,
                coachGymsListDataListener);
    }
}
