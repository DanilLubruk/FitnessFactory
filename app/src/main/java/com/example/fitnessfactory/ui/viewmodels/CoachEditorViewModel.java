package com.example.fitnessfactory.ui.viewmodels;

import android.content.Intent;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.dataListeners.DataListenerStringArgument;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;

import javax.inject.Inject;

public class CoachEditorViewModel extends PersonnelEditorViewModel {

    @Inject
    public CoachEditorViewModel(OwnerPersonnelRepository ownerRepository,
                                PersonnelAccessManager accessManager,
                                PersonnelDataManager dataManager,
                                DataListenerStringArgument dataListener) {
        super(ownerRepository, accessManager, dataManager, dataListener);
        //FFApp.get().getAppComponent().inject(this);
    }

    protected AppUser getPersonnelFromData(Intent personnelData) {
        AppUser coach = new AppUser();
        coach.setId(personnelData.getStringExtra(AppConsts.COACH_ID_EXTRA));
        coach.setName(personnelData.getStringExtra(AppConsts.COACH_NAME_EXTRA));
        coach.setEmail(personnelData.getStringExtra(AppConsts.COACH_EMAIL_EXTRA));

        return coach;
    }
}
