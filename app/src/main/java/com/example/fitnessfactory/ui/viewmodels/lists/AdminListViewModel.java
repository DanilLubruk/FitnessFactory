package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.data.dataListeners.AdminsListDataListener;
import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;

import javax.inject.Inject;

public class AdminListViewModel extends PersonnelListViewModel {

    @Inject
    public AdminListViewModel(AdminsAccessManager accessManager,
                              AdminsDataManager dataManager,
                              AdminsListDataListener dataListener) {
        super(accessManager, dataManager, dataListener);
        //FFApp.get().getAppComponent().inject(this);
    }
}
