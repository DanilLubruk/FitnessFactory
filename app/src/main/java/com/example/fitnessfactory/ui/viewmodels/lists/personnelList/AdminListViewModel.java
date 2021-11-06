package com.example.fitnessfactory.ui.viewmodels.lists.personnelList;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.AdminsListDataListener;
import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class AdminListViewModel extends PersonnelListViewModel {

    @Inject
    public AdminListViewModel(AdminsAccessManager accessManager,
                              AdminsDataManager dataManager,
                              AdminsListDataListener dataListener) {
        super(accessManager, dataManager, dataListener);
    }


    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak().concat(ResUtils.getString(R.string.message_error_admin_null));
    }
}
