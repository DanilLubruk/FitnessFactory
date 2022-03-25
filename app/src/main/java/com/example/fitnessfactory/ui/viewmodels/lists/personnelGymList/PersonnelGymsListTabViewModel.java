package com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.ArgDataListener;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.ui.viewmodels.lists.ListViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;

import java.util.List;

public abstract class PersonnelGymsListTabViewModel extends ListViewModel<Gym> {

    private final OwnerPersonnelRepository ownerRepository;
    private final ArgDataListener<String> dataListener;
    protected PersonnelDataManager dataManager;

    private final MutableLiveData<List<Gym>> gyms = new MutableLiveData<>();

    public PersonnelGymsListTabViewModel(OwnerPersonnelRepository ownerRepository,
                                         ArgDataListener<String> dataListener,
                                         PersonnelDataManager dataManager) {
        this.ownerRepository = ownerRepository;
        this.dataListener = dataListener;
        this.dataManager = dataManager;
    }

    protected OwnerPersonnelRepository getOwnerRepository() {
        return ownerRepository;
    }

    protected ArgDataListener<String> getDataListener() {
        return dataListener;
    }

    protected PersonnelDataManager getDataManager() {
        return dataManager;
    }

    public MutableLiveData<List<Gym>> getGyms() {
        return gyms;
    }

    public void getGymsData(String personnelEmail) {
        if (StringUtils.isEmpty(personnelEmail)) {
            return;
        }

        subscribeInIOThread(getDataManager().getPersonnelGymsByEmail(personnelEmail),
                new SingleData<>(gyms::setValue, getErrorHandler()::handleError));
    }

    public void addGym(String personnelEmail, String gymId) {
        if (StringUtils.isEmpty(personnelEmail)) {
            handleItemOperationError();
            return;
        }
        if (StringUtils.isEmpty(gymId)) {
            handleGymOperationNullError();
            return;
        }

        subscribeInIOThread(getOwnerRepository().addGymToPersonnelAsync(personnelEmail, gymId));
    }

    private void handleGymOperationNullError() {
        GuiUtils.showMessage(getErrorOperationMessage().concat(" - ").concat(getGymNullError()));
    }

    @Override
    public void deleteItem(String personnelEmail, Gym gym) {
        if (StringUtils.isEmpty(personnelEmail)) {
            handleItemDeletingNullError();
            return;
        }
        if (gym == null) {
            handleGymDeletingNullError();
            return;
        }

        subscribeInIOThread(getOwnerRepository().removeGymFromPersonnelAsync(personnelEmail, gym.getId()));
    }

    private void handleGymDeletingNullError() {
        GuiUtils.showMessage(getErrorDeletingMessage().concat(" - ").concat(getGymNullError()));
    }

    public void startDataListener(String personnelEmail) {
        if (StringUtils.isEmpty(personnelEmail)) {
            doInterruptProgress.setValue(true);
            return;
        }

        getDataListener().startDataListener(personnelEmail);
    }

    public void stopDataListener() {
        getDataListener().stopDataListener();
    }

    private String getGymNullError() {
        return ResUtils.getString(R.string.message_error_gym_null);
    }
}
