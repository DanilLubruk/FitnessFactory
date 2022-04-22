package com.example.fitnessfactory.ui.viewmodels.lists.personnelList;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.ClientsListDataListener;
import com.example.fitnessfactory.data.managers.access.ClientsAccessManager;
import com.example.fitnessfactory.data.managers.data.ClientsDataManager;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class ClientsListViewModel extends PersonnelListViewModel {

    @Inject
    public ClientsListViewModel(ClientsAccessManager accessManager,
                                ClientsDataManager dataManager,
                                ClientsListDataListener dataListener) {
        super(accessManager, dataManager, dataListener);
    }

    @Override
    protected String getItemNullClause() {
        return ResUtils.getString(R.string.message_error_client_null);
    }
}
