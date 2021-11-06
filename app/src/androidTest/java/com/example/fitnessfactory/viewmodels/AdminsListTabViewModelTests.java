package com.example.fitnessfactory.viewmodels;

import com.example.fitnessfactory.data.dataListeners.ArgDataListener;
import com.example.fitnessfactory.data.dataListeners.GymAdminsListDataListener;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.mockHelpers.mockers.data.AdminsDataManagerMocker;
import com.example.fitnessfactory.ui.viewmodels.lists.gymPersonnelList.GymAdminsListTabViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.gymPersonnelList.GymPersonnelListTabViewModel;

import org.mockito.Mockito;

public class AdminsListTabViewModelTests extends PersonnelListTabViewModelTests {

    private OwnerAdminsRepository ownerRepository = Mockito.mock(OwnerAdminsRepository.class);

    private AdminsDataManager dataManager;

    private GymAdminsListDataListener dataListener = Mockito.mock(GymAdminsListDataListener.class);

    @Override
    protected GymPersonnelListTabViewModel initViewModel() {
        dataManager = AdminsDataManagerMocker.createMock(ownerRepository, userRepository, gymRepository);

        return new GymAdminsListTabViewModel(ownerRepository, dataManager, dataListener);
    }

    @Override
    protected OwnerPersonnelRepository getOwnerRepository() {
        return ownerRepository;
    }

    @Override
    protected PersonnelDataManager getPersonnelDataManager() {
        return dataManager;
    }

    @Override
    protected ArgDataListener getDataListener() {
        return dataListener;
    }
}
