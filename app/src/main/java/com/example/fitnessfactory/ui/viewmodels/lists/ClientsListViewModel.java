package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.ClientsListDataListener;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.data.repositories.ownerData.ClientsRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class ClientsListViewModel extends BaseViewModel implements DataListListener<Client> {

    private final ClientsRepository clientsRepository;
    private final ClientsListDataListener dataListener;

    @Inject
    public ClientsListViewModel(ClientsListDataListener dataListener,
                                ClientsRepository clientsRepository) {
        this.dataListener = dataListener;
        this.clientsRepository = clientsRepository;
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
            GuiUtils.showMessage(ResUtils.getString(R.string.message_error_client_null));
            return;
        }

        subscribeInIOThread(clientsRepository.deleteClientCompletable(item));
    }
}
