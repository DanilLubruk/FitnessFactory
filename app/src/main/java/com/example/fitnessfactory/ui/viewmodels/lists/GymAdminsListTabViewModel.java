package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.data.dataListeners.GymAdminsListDataListener;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import javax.inject.Inject;

public class GymAdminsListTabViewModel extends GymPersonnelListTabViewModel {

    @Inject
    public GymAdminsListTabViewModel(OwnerAdminsRepository ownerRepository,
                                     AdminsDataManager dataManager,
                                     GymAdminsListDataListener dataListener) {
        super(ownerRepository, dataManager, dataListener);
    }
}
