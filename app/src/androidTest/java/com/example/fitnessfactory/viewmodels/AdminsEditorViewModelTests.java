package com.example.fitnessfactory.viewmodels;

import android.content.Intent;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.dataListeners.DataListenerStringArgument;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.ui.viewmodels.AdminEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.PersonnelEditorViewModel;

public class AdminsEditorViewModelTests extends PersonnelEditorViewModelTests {

    @Override
    protected PersonnelEditorViewModel getViewModel(OwnerPersonnelRepository ownerRepository,
                                                    PersonnelAccessManager accessManager,
                                                    PersonnelDataManager dataManager,
                                                    DataListenerStringArgument dataListener) {
        return new AdminEditorViewModel(ownerRepository, accessManager, dataManager, dataListener);
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
