package com.example.fitnessfactory.ui.viewmodels.lists;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.dataListeners.GymAdminsListDataListener;
import com.example.fitnessfactory.data.managers.AdminsDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.repositories.OwnerAdminsRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

public class AdminsListTabViewModel extends BaseViewModel implements DataListListener<AppUser> {

    @Inject
    OwnerAdminsRepository ownerAdminsRepository;
    @Inject
    AdminsDataManager adminsDataManager;
    @Inject
    GymAdminsListDataListener gymAdminsListDataListener;

    private final MutableLiveData<List<AppUser>> admins = new MutableLiveData<>();
    private String gymId;

    public AdminsListTabViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public MutableLiveData<List<AppUser>> getAdmins() {
        return admins;
    }

    public void addAdminToGym(String adminEmail) {
        if (TextUtils.isEmpty(gymId)) {
            return;
        }

        subscribeInIOThread(ownerAdminsRepository.addGymToAdminAsync(adminEmail, gymId));
    }

    public void deleteItem(AppUser admin) {
        if (TextUtils.isEmpty(gymId)) {
            return;
        }

        subscribeInIOThread(ownerAdminsRepository.removeGymFromAdminAsync(admin.getEmail(), gymId));
    }

    public void startDataListener() {
        if (TextUtils.isEmpty(gymId)) {
            return;
        }

        gymAdminsListDataListener.startDataListener(gymId);
    }

    public void stopDataListener() {
        gymAdminsListDataListener.stopDataListener();
    }

    public void setGymData(String gymId) {
        this.gymId = gymId;
    }

    public void getAdminsData() {
        if (TextUtils.isEmpty(gymId)) {
            return;
        }

        subscribeInIOThread(
                adminsDataManager.getAdminsListByGymIdAsync(gymId),
                new SingleData<>(admins::setValue, RxUtils::handleError));
    }

    @Override
    public void saveState(Bundle savedState) {
        super.saveState(savedState);
        getHandle().put(AppConsts.GYM_ID_EXTRA, gymId);
    }

    @Override
    public void restoreState(Bundle savedState) {
        super.restoreState(savedState);
        gymId = (String) getHandle().get(AppConsts.GYM_ID_EXTRA);
    }
}
