package com.example.fitnessfactory.viewmodels;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.dataListeners.AdminGymsListDataListener;
import com.example.fitnessfactory.data.dataListeners.DataListenerStringArgument;
import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.mockHelpers.mockers.AdminsAccessManagerMocker;
import com.example.fitnessfactory.mockHelpers.mockers.AdminsDataManagerMocker;
import com.example.fitnessfactory.ui.viewmodels.AdminEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.PersonnelEditorViewModel;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class AdminsEditorViewModelTests extends PersonnelEditorViewModelTests {

    private AdminsAccessRepository accessRepository = Mockito.mock(AdminsAccessRepository.class);

    private OwnerAdminsRepository ownerRepository = Mockito.mock(OwnerAdminsRepository.class);

    private AdminsAccessManager accessManager;

    private AdminsDataManager dataManager;

    private AdminGymsListDataListener dataListener = Mockito.mock(AdminGymsListDataListener.class);

    @Override
    protected PersonnelEditorViewModel getViewModel() {
        accessManager =
                AdminsAccessManagerMocker.createMock(accessRepository, ownerRepository);

        dataManager = AdminsDataManagerMocker.createMock(ownerRepository, userRepository, gymRepository);

        return new AdminEditorViewModel(ownerRepository, accessManager, dataManager, dataListener);
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

        intent.putExtra(AppConsts.ADMIN_ID_EXTRA, personnel.getId());
        intent.putExtra(AppConsts.ADMIN_NAME_EXTRA, personnel.getName());
        intent.putExtra(AppConsts.ADMIN_EMAIL_EXTRA, personnel.getEmail());

        return intent;
    }
}
