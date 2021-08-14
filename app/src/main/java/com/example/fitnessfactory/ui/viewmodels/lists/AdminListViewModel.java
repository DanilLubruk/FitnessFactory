package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.dataListeners.AdminsListDataListener;
import com.example.fitnessfactory.data.managers.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.AdminsDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.utils.RxUtils;

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
        subscribeInIOThread(
                adminsAccessManager.createAdmin(AppPrefs.gymOwnerId().getValue(), email),
                RxUtils::handleError);
    }

    public void addAdminsListListener() {
        adminsListListener.setAdminsListListener();
    }

    public MutableLiveData<List<AppUser>> getAdmins() {
        return admins;
    }

    public void getAdminsListData() {
        subscribeInIOThread(adminsDataManager.getAdminsListAsync(),
                new SingleData<>(admins::setValue, RxUtils::handleError));
    }

    public void removeAdminsListListener() {
        adminsListListener.removeDataListener();
    }

    public void deleteAdmin(String email) {
        subscribeInIOThread(
                adminsAccessManager.deleteAdminCompletable(AppPrefs.gymOwnerId().getValue(), email),
                RxUtils::handleError);
    }
}
