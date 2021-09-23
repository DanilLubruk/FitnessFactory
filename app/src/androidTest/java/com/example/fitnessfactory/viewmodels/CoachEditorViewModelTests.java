package com.example.fitnessfactory.viewmodels;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.dataListeners.DataListenerStringArgument;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.ui.viewmodels.CoachEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.PersonnelEditorViewModel;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CoachEditorViewModelTests extends PersonnelEditorViewModelTests {

    @Override
    protected PersonnelEditorViewModel getViewModel(OwnerPersonnelRepository ownerRepository,
                                                    PersonnelAccessManager accessManager,
                                                    PersonnelDataManager dataManager,
                                                    DataListenerStringArgument dataListener) {
        return new CoachEditorViewModel(ownerRepository, accessManager, dataManager, dataListener);
    }

    @Override
    protected Intent getDataIntent(AppUser appUser) {
        Intent intent = new Intent();

        intent.putExtra(AppConsts.COACH_ID_EXTRA, personnel.getId());
        intent.putExtra(AppConsts.COACH_NAME_EXTRA, personnel.getName());
        intent.putExtra(AppConsts.COACH_EMAIL_EXTRA, personnel.getEmail());

        return intent;
    }
}
