package com.example.fitnessfactory.viewmodels;

import com.example.fitnessfactory.data.dataListeners.AdminsListDataListener;
import com.example.fitnessfactory.data.dataListeners.DataListener;
import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.access.PersonnelAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.mockHelpers.mockers.access.AdminsAccessManagerMocker;
import com.example.fitnessfactory.mockHelpers.mockers.data.AdminsDataManagerMocker;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelList.AdminListViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelList.PersonnelListViewModel;

import org.mockito.Mockito;

public class AdminsListViewModelTests extends PersonnelListViewModelTests {

    private OwnerAdminsRepository ownerRepository = Mockito.mock(OwnerAdminsRepository.class);

    private AdminsAccessRepository accessRepository = Mockito.mock(AdminsAccessRepository.class);

    private AdminsListDataListener dataListener = Mockito.mock(AdminsListDataListener.class);

    private AdminsDataManager dataManager;

    private AdminsAccessManager accessManager;

    @Override
    protected PersonnelListViewModel initViewModel() {
        dataManager = AdminsDataManagerMocker.createMock(ownerRepository, userRepository, gymRepository);
        accessManager = AdminsAccessManagerMocker.createMock(accessRepository, ownerRepository);

        return new AdminListViewModel(accessManager, dataManager, dataListener);
    }

    @Override
    protected PersonnelAccessManager getAccessManager() {
        return accessManager;
    }

    @Override
    protected PersonnelDataManager getDataManager() {
        return dataManager;
    }

    @Override
    protected PersonnelAccessRepository getAccessRepository() {
        return accessRepository;
    }

    @Override
    protected OwnerPersonnelRepository getOwnerRepository() {
        return ownerRepository;
    }


    @Override
    protected DataListener getDataListener() {
        return dataListener;
    }
}
