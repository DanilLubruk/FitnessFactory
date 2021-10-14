package com.example.fitnessfactory.ui.viewmodels.editors;

import android.content.Intent;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.dataListeners.AdminGymsListDataListener;
import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class AdminEditorViewModel extends PersonnelEditorViewModel {

    @Inject
    public AdminEditorViewModel(OwnerAdminsRepository ownerRepository,
                                AdminsAccessManager accessManager,
                                AdminsDataManager dataManager,
                                AdminGymsListDataListener dataListener) {
        super(ownerRepository, accessManager, dataManager, dataListener);
    }

    @Override
    protected AppUser getPersonnelFromData(Intent personnelData) {
        AppUser admin = new AppUser();
        admin.setId(personnelData.getStringExtra(AppConsts.ADMIN_ID_EXTRA));
        admin.setName(personnelData.getStringExtra(AppConsts.ADMIN_NAME_EXTRA));
        admin.setEmail(personnelData.getStringExtra(AppConsts.ADMIN_EMAIL_EXTRA));

        return admin;
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak()
                .concat(ResUtils.getString(R.string.message_error_admin_null));
    }
}
