package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.data.dataListeners.CoachesListDataListener;
import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;

import javax.inject.Inject;

public class CoachesListViewModel extends PersonnelListViewModel {

    @Inject
    public CoachesListViewModel(CoachesAccessManager accessManager,
                                CoachesDataManager dataManager,
                                CoachesListDataListener dataListener) {
        super(accessManager, dataManager, dataListener);
    }
}
