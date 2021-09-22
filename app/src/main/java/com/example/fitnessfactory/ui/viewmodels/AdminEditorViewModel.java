package com.example.fitnessfactory.ui.viewmodels;

import android.content.Intent;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.dataListeners.DataListenerStringArgument;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;

import javax.inject.Inject;

public class AdminEditorViewModel extends PersonnelEditorViewModel {

    @Inject
    public AdminEditorViewModel(OwnerPersonnelRepository ownerRepository,
                                PersonnelAccessManager accessManager,
                                PersonnelDataManager dataManager,
                                DataListenerStringArgument dataListener) {
        super(ownerRepository, accessManager, dataManager, dataListener);
        //FFApp.get().getAppComponent().inject(this);
    }

    @Override
    protected AppUser getPersonnelFromData(Intent personnelData) {
        AppUser admin = new AppUser();
        admin.setId(personnelData.getStringExtra(AppConsts.ADMIN_ID_EXTRA));
        admin.setName(personnelData.getStringExtra(AppConsts.ADMIN_NAME_EXTRA));
        admin.setEmail(personnelData.getStringExtra(AppConsts.ADMIN_EMAIL_EXTRA));

        return admin;
    }
}
