package com.example.fitnessfactory.ui.viewmodels.lists;
import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.dataListeners.GymAdminsListDataListener;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import javax.inject.Inject;

public class AdminsListTabViewModel extends PersonnelListTabViewModel {

    @Inject
    OwnerAdminsRepository ownerAdminsRepository;
    @Inject
    AdminsDataManager adminsDataManager;
    @Inject
    GymAdminsListDataListener gymAdminsListDataListener;

    public AdminsListTabViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    protected OwnerAdminsRepository getOwnerRepository() {
        return ownerAdminsRepository;
    }

    protected AdminsDataManager getDataManager() {
        return adminsDataManager;
    }

    protected GymAdminsListDataListener getDataListener() {
        return gymAdminsListDataListener;
    }
}
