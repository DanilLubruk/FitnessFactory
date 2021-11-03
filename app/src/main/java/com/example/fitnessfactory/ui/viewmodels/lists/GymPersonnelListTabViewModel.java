package com.example.fitnessfactory.ui.viewmodels.lists;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.dataListeners.ArgDataListener;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.List;

public abstract class GymPersonnelListTabViewModel extends BaseViewModel implements DataListListener<AppUser> {

    private final OwnerPersonnelRepository ownerRepository;

    private final PersonnelDataManager dataManager;

    private final ArgDataListener<String> dataListener;

    private final MutableLiveData<List<AppUser>> personnel = new MutableLiveData<>();

    private String gymId;

    public GymPersonnelListTabViewModel(OwnerPersonnelRepository ownerRepository,
                                        PersonnelDataManager dataManager,
                                        ArgDataListener<String> dataListener) {
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

    protected ArgDataListener<String> getDataListener() {
        return dataListener;
    }

    public MutableLiveData<List<AppUser>> getPersonnel() {
        return personnel;
    }

    public void resetGymId(String gymId) {
        this.gymId = gymId;
    }

    public void addPersonnelToGym(String personnelEmail) {
        if (TextUtils.isEmpty(gymId)) {
            GuiUtils.showMessage(getGymNullMessage());
            return;
        }

        subscribeInIOThread(getOwnerRepository().addGymToPersonnelAsync(personnelEmail, gymId));
    }

    public void deleteItem(AppUser personnel) {
        if (TextUtils.isEmpty(gymId)) {
            GuiUtils.showMessage(getGymNullMessage());
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

    public void getPersonnelData() {
        if (TextUtils.isEmpty(gymId)) {
            GuiUtils.showMessage(getGymNullMessage());
            return;
        }

        subscribeInIOThread(
                getDataManager().getPersonnelListByGymIdAsync(gymId),
                new SingleData<>(personnel::setValue, getErrorHandler()::handleError));
    }

    private String getGymNullMessage() {
        return ResUtils.getString(R.string.message_error_gym_null);
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
