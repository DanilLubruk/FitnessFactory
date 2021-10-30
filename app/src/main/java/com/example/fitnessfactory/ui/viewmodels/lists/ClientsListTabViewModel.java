package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.SessionClientsListDataListener;
import com.example.fitnessfactory.data.managers.data.SessionsDataManager;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListenerStringArgument;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;

import javax.inject.Inject;

public class ClientsListTabViewModel extends BaseViewModel implements DataListListenerStringArgument<Client> {

    private final SessionsDataManager sessionsDataManager;
    private final SessionClientsListDataListener dataListener;

    @Inject
    public ClientsListTabViewModel(SessionsDataManager sessionsDataManager,
                                   SessionClientsListDataListener dataListener) {
        this.sessionsDataManager = sessionsDataManager;
        this.dataListener = dataListener;
    }

    @Override
    public void startDataListener(String sessionId) {
        dataListener.startDataListener(sessionId);
    }

    @Override
    public void stopDataListener() {
        dataListener.stopDataListener();
    }

    public void addClientToSession(String sessionId, String clientId) {
        if (StringUtils.isEmpty(sessionId)) {
            GuiUtils.showMessage(ResUtils.getString(R.string.message_error_session_null));
            return;
        }
        if (StringUtils.isEmpty(clientId)) {
            GuiUtils.showMessage(ResUtils.getString(R.string.message_error_client_null));
            return;
        }

        subscribeInIOThread(
                sessionsDataManager.addClientToSession(sessionId, clientId),
                getErrorHandler()::handleError);
    }

    @Override
    public void deleteItem(Client item) {

    }
}
