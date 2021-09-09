package com.example.fitnessfactory.ui.viewmodels;

import android.content.Intent;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.dataListeners.DataListenerStringArgument;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.utils.RxUtils;

import java.util.List;

public abstract class PersonnelEditorViewModel extends EditorViewModel implements DataListListener<Gym> {

    public final ObservableField<AppUser> personnel = new ObservableField<>();
    private final MutableLiveData<List<Gym>> gyms = new MutableLiveData<>();

    protected abstract OwnerPersonnelRepository getOwnerRepository();

    protected abstract PersonnelAccessManager getAccessManager();

    protected abstract PersonnelDataManager getDataManager();

    protected abstract DataListenerStringArgument getDataListener();

    protected abstract AppUser getPersonnelFromData(Intent personnelData);

    public void setPersonnelData(Intent personnelData) {
        AppUser personnel = getPersonnelFromData(personnelData);

        this.personnel.set(personnel);
    }

    public void addGym(String gymId) {
        AppUser personnel = this.personnel.get();
        if (personnel == null) {
            return;
        }

        subscribeInIOThread(
                getOwnerRepository().addGymToPersonnel(personnel.getEmail(), gymId),
                RxUtils::handleError);
    }

    @Override
    public void deleteItem(Gym gym) {
        AppUser personnel = this.personnel.get();
        if (personnel == null) {
            return;
        }

        subscribeInIOThread(
                getOwnerRepository().removeGymFromPersonnel(personnel.getEmail(), gym.getId()),
                RxUtils::handleError);
    }

    public MutableLiveData<List<Gym>> getGyms() {
        return gyms;
    }

    public void startDataListener() {
        AppUser personnel = this.personnel.get();
        if (personnel == null) {
            return;
        }

        getDataListener().startDataListener(personnel.getEmail());
    }

    public void stopDataListener() {
        getDataListener().stopDataListener();
    }

    public void getGymsData() {
        AppUser personnel = this.personnel.get();
        if (personnel == null) {
            return;
        }

        subscribeInIOThread(getDataManager().getPersonnelGymsByEmail(personnel.getEmail()),
                new SingleData<>(gyms::setValue, RxUtils::handleError));
    }

    @Override
    public SingleLiveEvent<Boolean> isModified() {
        SingleLiveEvent<Boolean> isModified = new SingleLiveEvent<>();
        isModified.setValue(false);

        return isModified;
    }

    @Override
    public SingleLiveEvent<Boolean> save() {
        SingleLiveEvent<Boolean> isSaved = new SingleLiveEvent<>();
        isSaved.setValue(true);

        return isSaved;
    }

    @Override
    public SingleLiveEvent<Boolean> delete() {
        SingleLiveEvent<Boolean> isDeleted = new SingleLiveEvent<>();

        AppUser admin = this.personnel.get();
        if (admin == null) {
            isDeleted.setValue(false);
            return isDeleted;
        }

        subscribeInIOThread(
                getAccessManager().deletePersonnelSingle(AppPrefs.gymOwnerId().getValue(), admin.getEmail()),
                new SingleData<>(isDeleted::setValue, throwable -> RxUtils.handleError(isDeleted, throwable)));

        return isDeleted;
    }
}