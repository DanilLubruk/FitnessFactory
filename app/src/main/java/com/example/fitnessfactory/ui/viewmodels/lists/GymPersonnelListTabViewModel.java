package com.example.fitnessfactory.ui.viewmodels.lists;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.dataListeners.ArgDataListener;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;

import java.util.List;

public abstract class GymPersonnelListTabViewModel extends BaseViewModel implements DataListListener<AppUser> {

    private OwnerPersonnelRepository ownerRepository;

    private PersonnelDataManager dataManager;

    private ArgDataListener dataListener;

    public GymPersonnelListTabViewModel(OwnerPersonnelRepository ownerRepository,
                                        PersonnelDataManager dataManager,
                                        ArgDataListener dataListener) {
        this.ownerRepository = ownerRepository;
        this.dataManager = dataManager;
        this.dataListener = dataListener;
    }

    protected OwnerPersonnelRepository getOwnerRepository() {
        return ownerRepository;
    }

    protected PersonnelDataManager getDataManager() {
        return dataManager;
    }

    protected ArgDataListener getDataListener() {
        return dataListener;
    }

    private final MutableLiveData<List<AppUser>> personnel = new MutableLiveData<>();

    private String gymId;

    public MutableLiveData<List<AppUser>> getPersonnel() {
        return personnel;
    }

    public void addPersonnelToGym(String personnelEmail) {
        if (TextUtils.isEmpty(gymId)) {
            return;
        }

        subscribeInIOThread(getOwnerRepository().addGymToPersonnelAsync(personnelEmail, gymId));
    }

    public void deleteItem(AppUser personnel) {
        if (TextUtils.isEmpty(gymId)) {
            return;
        }

        subscribeInIOThread(getOwnerRepository().removeGymFromPersonnelAsync(personnel.getEmail(), gymId));
    }

    public void startDataListener() {
        if (TextUtils.isEmpty(gymId)) {
            return;
        }

        getDataListener().startDataListener(gymId);
    }

    public void stopDataListener() {
        getDataListener().stopDataListener();
    }

    public void refreshGymData(String gymId) {
        this.gymId = gymId;
    }

    public void getPersonnelData() {
        if (TextUtils.isEmpty(gymId)) {
            return;
        }

        subscribeInIOThread(
                getDataManager().getPersonnelListByGymIdAsync(gymId),
                new SingleData<>(personnel::setValue, getErrorHandler()::handleError));
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
