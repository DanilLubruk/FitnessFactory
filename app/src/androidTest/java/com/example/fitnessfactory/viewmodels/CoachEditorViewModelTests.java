package com.example.fitnessfactory.viewmodels;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.dataListeners.CoachGymsListDataListener;
import com.example.fitnessfactory.data.dataListeners.DataListenerStringArgument;
import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.access.CoachesAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.mockHelpers.mockers.access.CoachesAccessManagerMocker;
import com.example.fitnessfactory.mockHelpers.mockers.data.CoachesDataManagerMocker;
import com.example.fitnessfactory.ui.viewmodels.CoachEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.PersonnelEditorViewModel;

import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class CoachEditorViewModelTests extends PersonnelEditorViewModelTests {

    private CoachesAccessRepository accessRepository = Mockito.mock(CoachesAccessRepository.class);

    private OwnerCoachesRepository ownerRepository = Mockito.mock(OwnerCoachesRepository.class);

    private CoachGymsListDataListener dataListener = Mockito.mock(CoachGymsListDataListener.class);

    private CoachesAccessManager accessManager;

    private CoachesDataManager dataManager;

    @Override
    protected PersonnelEditorViewModel getViewModel() {
        accessManager =
                CoachesAccessManagerMocker.createMock(accessRepository, ownerRepository);

        dataManager = CoachesDataManagerMocker.createMock(ownerRepository, userRepository, gymRepository);

        return new CoachEditorViewModel(ownerRepository, accessManager, dataManager, dataListener);
    }

    @Override
    protected OwnerPersonnelRepository getOwnerRepository() {
        return ownerRepository;
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
    protected DataListenerStringArgument getDataListener() {
        return dataListener;
    }

    @Override
    protected Intent getDataIntent(AppUser appUser) {
        Intent intent = new Intent();

        intent.putExtra(AppConsts.COACH_ID_EXTRA, personnel.getId());
        intent.putExtra(AppConsts.COACH_NAME_EXTRA, personnel.getName());
        intent.putExtra(AppConsts.COACH_EMAIL_EXTRA, personnel.getEmail());

        return intent;
    }
}
