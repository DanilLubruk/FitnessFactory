package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.ClientsListDataListener;
import com.example.fitnessfactory.data.managers.data.ClientsDataManager;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.data.repositories.ownerData.ClientsRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class ClientsListViewModel extends ListViewModel<Client>{

    private final ClientsDataManager clientsDataManager;
    private final ClientsListDataListener dataListener;

    @Inject
    public ClientsListViewModel(ClientsDataManager clientsDataManager,
                                ClientsListDataListener dataListener) {
        this.clientsDataManager = clientsDataManager;
        this.dataListener = dataListener;
    }

    @Override
    public void startDataListener() {
        dataListener.startDataListener();
    }

    @Override
    public void stopDataListener() {
        dataListener.stopDataListener();
    }

    @Override
    public void deleteItem(Client item) {
        if (item == null) {
            handleItemDeletingNullError();
            return;
        }

        subscribeInIOThread(clientsDataManager.deleteClientCompletable(item));
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak().concat(ResUtils.getString(R.string.message_error_client_null));
    }
}
