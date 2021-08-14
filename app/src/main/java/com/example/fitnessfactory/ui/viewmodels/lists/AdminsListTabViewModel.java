package com.example.fitnessfactory.ui.viewmodels.lists;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.repositories.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.bondingRepositories.AdminAccessRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

public class AdminsListTabViewModel extends BaseViewModel {

    @Inject
    AdminsAccessRepository adminsAccessRepository;
    @Inject
    AdminAccessRepository adminAccessRepository;

    private MutableLiveData<List<AppUser>> admins = new MutableLiveData<>();
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

        subscribeInIOThread(
                adminsAccessRepository.addGymToPersonnelAsync(AppPrefs.gymOwnerId().getValue(), adminEmail, gymId),
                RxUtils::handleError);
    }

    public void removeAdminFromGym(String adminEmail) {
        if (TextUtils.isEmpty(gymId)) {
            return;
        }

        subscribeInIOThread(
                adminsAccessRepository.removeGymFromPersonnelAsync(AppPrefs.gymOwnerId().getValue(), adminEmail, gymId),
                RxUtils::handleError);
    }

    public void addGymAdminsListListener() {
        if (TextUtils.isEmpty(gymId)) {
            return;
        }

        subscribeInIOThread(
                adminAccessRepository.addGymAdminsListListener(AppPrefs.gymOwnerId().getValue(), gymId),
                RxUtils::handleError);
    }

    public void removeGymAdminsListListener() {
        subscribeInIOThread(adminAccessRepository.removeGymAdminsListListener(), RxUtils::handleError);
    }

    public void setGymData(String gymId) {
        this.gymId = gymId;
    }

    public void getAdminsData() {
        if (TextUtils.isEmpty(gymId)) {
            return;
        }

        subscribeInIOThread(
                adminAccessRepository.getAdminsByGymIdAsync(AppPrefs.gymOwnerId().getValue(), gymId),
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
