package com.example.fitnessfactory.viewmodels;

import com.example.fitnessfactory.data.dataListeners.CoachesListDataListener;
import com.example.fitnessfactory.data.dataListeners.DataListener;
import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.repositories.access.CoachesAccessRepository;
import com.example.fitnessfactory.data.repositories.access.PersonnelAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.data.repositories.ownerData.participantsData.CoachSessionsRepository;
import com.example.fitnessfactory.mockHelpers.mockers.access.CoachesAccessManagerMocker;
import com.example.fitnessfactory.mockHelpers.mockers.data.CoachesDataManagerMocker;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelList.CoachesListViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelList.PersonnelListViewModel;

import org.mockito.Mockito;

public class CoachesListViewModelTests extends PersonnelListViewModelTests {

    private CoachSessionsRepository coachSessionsRepository = Mockito.mock(CoachSessionsRepository.class);

    private OwnerCoachesRepository ownerRepository = Mockito.mock(OwnerCoachesRepository.class);

    private CoachesAccessRepository accessRepository = Mockito.mock(CoachesAccessRepository.class);

    private CoachesListDataListener dataListener = Mockito.mock(CoachesListDataListener.class);

    private CoachesDataManager dataManager;

    private CoachesAccessManager accessManager;

    @Override
    protected PersonnelListViewModel initViewModel() {
        dataManager = CoachesDataManagerMocker
                .createMock(ownerRepository, userRepository, gymRepository);
        accessManager = CoachesAccessManagerMocker
                .createMock(coachSessionsRepository, accessRepository, ownerRepository);

        return new CoachesListViewModel(accessManager, dataManager, dataListener);
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
