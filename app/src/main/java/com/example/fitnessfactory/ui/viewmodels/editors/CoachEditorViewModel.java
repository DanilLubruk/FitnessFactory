package com.example.fitnessfactory.ui.viewmodels.editors;

import android.content.Intent;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.dataListeners.CoachGymsListDataListener;
import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class CoachEditorViewModel extends PersonnelEditorViewModel {

    @Inject
    public CoachEditorViewModel(CoachesAccessManager accessManager) {
        super(accessManager);
    }

    protected AppUser getPersonnelFromData(Intent personnelData) {
        AppUser coach = new AppUser();
        coach.setId(personnelData.getStringExtra(AppConsts.COACH_ID_EXTRA));
        coach.setName(personnelData.getStringExtra(AppConsts.COACH_NAME_EXTRA));
        coach.setEmail(personnelData.getStringExtra(AppConsts.COACH_EMAIL_EXTRA));

        return coach;
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak()
                .concat(ResUtils.getString(R.string.message_error_coach_null));
    }
}
