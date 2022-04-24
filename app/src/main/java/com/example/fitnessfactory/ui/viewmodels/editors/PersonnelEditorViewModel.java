package com.example.fitnessfactory.ui.viewmodels.editors;

import android.content.Intent;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;

public abstract class PersonnelEditorViewModel extends EditorViewModel {

    public final ObservableField<AppUser> personnel = new ObservableField<>();
    public final MutableLiveData<String> personnelId = new MutableLiveData<>("");

    protected PersonnelAccessManager accessManager;

    public PersonnelEditorViewModel(PersonnelAccessManager accessManager) {
        this.accessManager = accessManager;
    }

    protected PersonnelAccessManager getAccessManager() {
        return accessManager;
    }

    protected abstract AppUser getPersonnelFromData(Intent personnelData);

    public void setPersonnelData(Intent personnelData) {
        AppUser personnel = getPersonnelFromData(personnelData);

        this.personnel.set(personnel);
        this.personnelId.setValue(personnel.getId());
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
        personnelId.setValue(personnel.getId());

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
                getAccessManager().deletePersonnelSingle(AppPrefs.gymOwnerId().getValue(), personnel.getId()),
                new SingleData<>(
                        isDeleted::setValue,
                        throwable -> getErrorHandler().handleError(isDeleted, throwable)));

        return isDeleted;
    }
}
