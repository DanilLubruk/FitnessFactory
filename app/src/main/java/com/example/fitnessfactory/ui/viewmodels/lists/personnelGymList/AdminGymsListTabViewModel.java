package com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.AdminGymsListDataListener;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class AdminGymsListTabViewModel extends PersonnelGymsListTabViewModel {

    @Inject
    public AdminGymsListTabViewModel(OwnerAdminsRepository ownerRepository,
                                     AdminGymsListDataListener dataListener,
                                     AdminsDataManager adminsDataManager) {
        super(ownerRepository, dataListener, adminsDataManager);
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak().concat(ResUtils.getString(R.string.message_error_admin_null));
    }
}
