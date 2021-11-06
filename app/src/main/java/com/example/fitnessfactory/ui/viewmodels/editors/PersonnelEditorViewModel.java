package com.example.fitnessfactory.ui.viewmodels.editors;

import android.content.Intent;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.dataListeners.ArgDataListener;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;

import java.util.List;

public abstract class PersonnelEditorViewModel extends EditorViewModel {

    public final ObservableField<AppUser> personnel = new ObservableField<>();
    public final MutableLiveData<String> personnelEmail = new MutableLiveData<>();

    protected PersonnelAccessManager accessManager;

    public PersonnelEditorViewModel(PersonnelAccessManager accessManager) {
        this.accessManager = accessManager;
    }

    protected PersonnelAccessManager getAccessManager() {
        return accessManager;
    }

    protected abstract AppUser getPersonnelFromData(Intent personnelData);

    public MutableLiveData<String> getPersonnelEmail() {
        return personnelEmail;
    }

    public void setPersonnelData(Intent personnelData) {
        AppUser personnel = getPersonnelFromData(personnelData);

        this.personnel.set(personnel);
        this.personnelEmail.setValue(personnel.getEmail());
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

        AppUser personnel = this.personnel.get();
        if (personnel == null) {
            return handleItemSavingNullError(isSaved);
        }
        personnelEmail.setValue(personnel.getEmail());

        isSaved.setValue(true);

        return isSaved;
    }

    @Override
    public SingleLiveEvent<Boolean> delete() {
        SingleLiveEvent<Boolean> isDeleted = new SingleLiveEvent<>();

        AppUser personnel = this.personnel.get();
        if (personnel == null) {
            return handleItemDeletingNullError(isDeleted);
        }

        subscribeInIOThread(
                getAccessManager().deletePersonnelSingle(AppPrefs.gymOwnerId().getValue(), personnel.getEmail()),
                new SingleData<>(
                        isDeleted::setValue,
                        throwable -> getErrorHandler().handleError(isDeleted, throwable)));

        return isDeleted;
    }
}
