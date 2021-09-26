package com.example.fitnessfactory.ui.viewmodels;

import android.content.Intent;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.dataListeners.CoachGymsListDataListener;
import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;

import javax.inject.Inject;

public class CoachEditorViewModel extends PersonnelEditorViewModel {

    @Inject
    public CoachEditorViewModel(OwnerCoachesRepository ownerRepository,
                                CoachesAccessManager accessManager,
                                CoachesDataManager dataManager,
                                CoachGymsListDataListener dataListener) {
        super(ownerRepository, accessManager, dataManager, dataListener);
    }

    protected AppUser getPersonnelFromData(Intent personnelData) {
        AppUser coach = new AppUser();
        coach.setId(personnelData.getStringExtra(AppConsts.COACH_ID_EXTRA));
        coach.setName(personnelData.getStringExtra(AppConsts.COACH_NAME_EXTRA));
        coach.setEmail(personnelData.getStringExtra(AppConsts.COACH_EMAIL_EXTRA));

        return coach;
    }
}
