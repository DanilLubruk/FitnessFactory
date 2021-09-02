package com.example.fitnessfactory.ui.viewmodels;

import android.content.Intent;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.dataListeners.AdminGymsListDataListener;
import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.data.GymsDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

public class AdminEditorViewModel extends EditorViewModel implements DataListListener<Gym> {

    @Inject
    OwnerAdminsRepository ownerAdminsRepository;
    @Inject
    AdminsAccessManager adminsAccessManager;
    @Inject
    AdminGymsListDataListener adminGymsListDataListener;
    @Inject
    GymsDataManager gymsDataManager;

    public final ObservableField<AppUser> admin = new ObservableField<>();
    private final MutableLiveData<List<Gym>> gyms = new MutableLiveData<>();

    public AdminEditorViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public void setAdminData(Intent adminData) {
        AppUser admin = new AppUser();
        admin.setId(adminData.getStringExtra(AppConsts.ADMIN_ID_EXTRA));
        admin.setName(adminData.getStringExtra(AppConsts.ADMIN_NAME_EXTRA));
        admin.setEmail(adminData.getStringExtra(AppConsts.ADMIN_EMAIL_EXTRA));

        this.admin.set(admin);
    }

    public void addGym(String gymId) {
        AppUser admin = this.admin.get();
        if (admin == null) {
            return;
        }

        subscribeInIOThread(
                ownerAdminsRepository.addGymToAdminAsync(admin.getEmail(), gymId),
                RxUtils::handleError);
    }

    public void deleteItem(Gym gym) {
        AppUser admin = this.admin.get();
        if (admin == null) {
            return;
        }

        subscribeInIOThread(
                ownerAdminsRepository.removeGymFromAdminAsync(admin.getEmail(), gym.getId()),
                RxUtils::handleError);
    }

    public MutableLiveData<List<Gym>> getGyms() {
        return gyms;
    }

    public void startDataListener() {
        AppUser admin = this.admin.get();
        if (admin == null) {
            return;
        }

        adminGymsListDataListener.startDataListener(admin.getEmail());
    }

    public void stopDataListener() {
        adminGymsListDataListener.stopDataListener();
    }

    public void getGymsData() {
        AppUser admin = this.admin.get();
        if (admin == null) {
            return;
        }

        subscribeInIOThread(gymsDataManager.getAdminsGymsByEmail(admin.getEmail()),
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

        AppUser admin = this.admin.get();
        if (admin == null) {
            isDeleted.setValue(false);
            return isDeleted;
        }

        subscribeInIOThread(
                adminsAccessManager.deletePersonnelSingle(AppPrefs.gymOwnerId().getValue(), admin.getEmail()),
                new SingleData<>(isDeleted::setValue, throwable -> RxUtils.handleError(isDeleted, throwable)));

        return isDeleted;
    }
}
