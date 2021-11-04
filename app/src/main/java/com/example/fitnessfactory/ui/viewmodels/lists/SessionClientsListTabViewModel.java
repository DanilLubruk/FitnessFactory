package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.ArgDataListener;
import com.example.fitnessfactory.data.dataListeners.SessionClientsListDataListener;
import com.example.fitnessfactory.data.managers.data.SessionsDataManager;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.repositories.ownerData.ClientsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class SessionClientsListTabViewModel extends SessionParticipantListTabViewModel<Client> {

    private final SessionClientsListDataListener dataListener;
    private final ClientsRepository clientsRepository;

    private final MutableLiveData<List<Client>> clients = new MutableLiveData<>();

    @Inject
    public SessionClientsListTabViewModel(SessionsDataManager sessionsDataManager,
                                          ClientsRepository clientsRepository,
                                          SessionClientsListDataListener dataListener) {
        super(sessionsDataManager);
        this.clientsRepository = clientsRepository;
        this.dataListener = dataListener;
    }

    public LiveData<List<Client>> getClients() {
        return clients;
    }

    public void resetClientsList(List<String> clientsIds) {
        subscribeInIOThread(
                clientsRepository.getClientsAsync(clientsIds),
                new SingleData<>(clients::setValue, getErrorHandler()::handleError));
    }

    @Override
    protected ArgDataListener<String> getDataListener() {
        return dataListener;
    }

    @Override
    protected Completable getAddParticipantAction(String sessionId, String clientId) {
        return sessionsDataManager.addClientToSession(sessionId, clientId);
    }

    @Override
    protected Completable getDeleteParticipantAction(String sessionId, String clientId) {
        return sessionsDataManager.removeClientFromSession(sessionId, clientId);
    }

    @Override
    protected List<String> getParticipantsList(Session session) {
        return session.getClientsIds();
    }

    @Override
    protected String getParticipantId(Client client) {
        return client.getId();
    }

    @Override
    protected String getParticipantNullMessage() {
        return ResUtils.getString(R.string.message_error_client_null);
    }
}
