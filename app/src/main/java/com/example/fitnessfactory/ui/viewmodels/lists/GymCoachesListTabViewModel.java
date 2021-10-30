package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.data.dataListeners.GymCoachesListDataListener;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;

import javax.inject.Inject;

public class GymCoachesListTabViewModel extends GymPersonnelListTabViewModel {

    @Inject
    public GymCoachesListTabViewModel(OwnerCoachesRepository ownerRepository,
                                      CoachesDataManager dataManager,
                                      GymCoachesListDataListener dataListener) {
        super(ownerRepository, dataManager, dataListener);
    }
}
