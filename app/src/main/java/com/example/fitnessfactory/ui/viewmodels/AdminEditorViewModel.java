package com.example.fitnessfactory.ui.viewmodels;

import android.content.Intent;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.GymRepository;
import com.example.fitnessfactory.data.repositories.StaffAccessRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class AdminEditorViewModel extends EditorViewModel {

    @Inject
    GymRepository gymRepository;
    @Inject
    StaffAccessRepository accessRepository;

    public ObservableField<AppUser> admin = new ObservableField<>();
    private MutableLiveData<List<Gym>> gyms = new MutableLiveData<>();

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
                accessRepository.addGymToPersonnelAsync(
                        AppPrefs.gymOwnerId().getValue(),
                        admin.getEmail(),
                        gymId),
                this::handleError);
    }

    public void removeGym(String gymId) {
        AppUser admin = this.admin.get();
        if (admin == null) {
            return;
        }

        subscribeInIOThread(
                accessRepository.removeGymFromPersonnelAsync(
                        AppPrefs.gymOwnerId().getValue(),
                        admin.getEmail(),
                        gymId),
                this::handleError);
    }

    public MutableLiveData<List<Gym>> getGyms() {
        return gyms;
    }

    public void addAdminEditorGymsListener() {
        AppUser admin = this.admin.get();
        if (admin == null) {
            return;
        }

        subscribeInIOThread(
                accessRepository.addAdminGymsListListener(AppPrefs.gymOwnerId().getValue(), admin.getEmail()),
                this::handleError);
    }

    public void removeAdminEditorGymsListener() {
        subscribeInIOThread(accessRepository.removeAdminGymsListListener(), this::handleError);
    }

    public void getGymsData() {
        AppUser admin = this.admin.get();
        if (admin == null) {
            return;
        }

        addSubscription(accessRepository.getPersonnelGymsAsync(AppPrefs.gymOwnerId().getValue(), admin.getEmail())
                .subscribeOn(getIOScheduler())
                .observeOn(getIOScheduler())
                .flatMap(gymsIds -> gymRepository.getGymsByIds(gymsIds))
                .observeOn(getMainThreadScheduler())
                .subscribe(gyms::setValue,
                        this::handleError));
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
                accessRepository.deleteAdminCompletable(AppPrefs.gymOwnerId().getValue(), admin.getEmail()),
                throwable -> handleError(isDeleted, throwable));

        return isDeleted;
    }
}
