package com.example.fitnessfactory.ui.viewmodels;

import android.content.Intent;
import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.dataListeners.AdminGymsListDataListener;
import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import javax.inject.Inject;

public class AdminEditorViewModel extends PersonnelEditorViewModel {

    @Inject
    OwnerAdminsRepository ownerAdminsRepository;
    @Inject
    AdminsAccessManager adminsAccessManager;
    @Inject
    AdminsDataManager adminsDataManager;
    @Inject
    AdminGymsListDataListener adminGymsListDataListener;

    public AdminEditorViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    @Override
    protected OwnerAdminsRepository getOwnerRepository() {
        return ownerAdminsRepository;
    }

    @Override
    protected AdminsAccessManager getAccessManager() {
        return adminsAccessManager;
    }

    @Override
    protected AdminsDataManager getDataManager() {
        return adminsDataManager;
    }

    @Override
    protected AdminGymsListDataListener getDataListener() {
        return adminGymsListDataListener;
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
