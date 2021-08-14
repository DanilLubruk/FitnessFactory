package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.dataListeners.AdminsListDataListener;
import com.example.fitnessfactory.data.managers.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.AdminsDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

public class AdminListViewModel extends BaseViewModel {

    @Inject
    AdminsAccessManager adminsAccessManager;
    @Inject
    AdminsDataManager adminsDataManager;
    @Inject
    AdminsListDataListener adminsListListener;

    private MutableLiveData<List<AppUser>> admins = new MutableLiveData<>();

    public AdminListViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public void registerAdmin(String email) {
        adminsAccessManager.createAdmin(AppPrefs.gymOwnerId().getValue(), email);
    }

    public void addAdminsListListener() {
        adminsListListener.setAdminsListListener();
    }

    public MutableLiveData<List<AppUser>> getAdmins() {
        return admins;
    }

    public void getAdminsListData() {
        admins.setValue(adminsDataManager.getAdminsListData());
    }

    public void removeAdminsListListener() {
        adminsListListener.removeAdminsListListener();
    }

    public void deleteAdmin(String email) {
        adminsAccessManager.deleteAdmin(AppPrefs.gymOwnerId().getValue(), email);
    }
}
