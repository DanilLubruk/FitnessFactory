package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.dataListeners.AdminsListDataListener;
import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;

import javax.inject.Inject;


public class AdminListViewModel extends PersonnelListViewModel {

    @Inject
    AdminsAccessManager adminsAccessManager;
    @Inject
    AdminsDataManager adminsDataManager;
    @Inject
    AdminsListDataListener adminsListListener;

    public AdminListViewModel() {
        FFApp.get().getAppComponent().inject(this);
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
    protected AdminsListDataListener getDataListener() {
        return adminsListListener;
    }
}
