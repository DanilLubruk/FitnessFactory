package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.dataListeners.DataListenerStringArgument;
import com.example.fitnessfactory.data.dataListeners.GymCoachesListDataListener;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;

import javax.inject.Inject;

public class CoachesListTabViewModel extends PersonnelListTabViewModel {

    @Inject
    OwnerCoachesRepository coachesRepository;
    @Inject
    CoachesDataManager coachesDataManager;
    @Inject
    GymCoachesListDataListener dataListener;

    public CoachesListTabViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    protected OwnerCoachesRepository getOwnerRepository() {
        return coachesRepository;
    }

    protected CoachesDataManager getDataManager() {
        return coachesDataManager;
    }

    protected DataListenerStringArgument getDataListener() {
        return dataListener;
    }
}
