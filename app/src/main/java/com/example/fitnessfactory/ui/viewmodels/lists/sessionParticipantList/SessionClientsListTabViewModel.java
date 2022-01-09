package com.example.fitnessfactory.ui.viewmodels.lists.sessionParticipantList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.ArgDataListener;
import com.example.fitnessfactory.data.dataListeners.SessionClientsListDataListener;
import com.example.fitnessfactory.data.managers.data.ClientsDataManager;
import com.example.fitnessfactory.data.managers.data.SessionsDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.ClientsRepository;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class SessionClientsListTabViewModel extends SessionParticipantListTabViewModel<AppUser> {

    private final UserRepository userRepository;
    private final SessionClientsListDataListener dataListener;
    private final ClientsRepository clientsRepository;

    private final MutableLiveData<List<AppUser>> clients = new MutableLiveData<>();

    @Inject
    public SessionClientsListTabViewModel(SessionsDataManager sessionsDataManager,
                                          ClientsRepository clientsRepository,
                                          UserRepository userRepository,
                                          SessionClientsListDataListener dataListener) {
        super(sessionsDataManager);
        this.clientsRepository = clientsRepository;
        this.userRepository = userRepository;
        this.dataListener = dataListener;
    }

    public LiveData<List<AppUser>> getClients() {
        return clients;
    }

    public void resetClientsList(List<String> clientsEmails) {
        subscribeInIOThread(
                userRepository.getUsersByEmailsAsync(clientsEmails),
                new SingleData<>(clients::setValue, getErrorHandler()::handleError));
    }

    @Override
    protected ArgDataListener<String> getDataListener() {
        return dataListener;
    }

    @Override
    protected Completable getAddParticipantAction(String sessionId, String clientEmail) {
        return sessionsDataManager.addClientToSession(sessionId, clientEmail);
    }

    @Override
    protected Completable getDeleteParticipantAction(String sessionId, String clientEmail) {
        return sessionsDataManager.removeClientFromSession(sessionId, clientEmail);
    }

    @Override
    protected List<String> getParticipantsList(Session session) {
        return session.getClientsEmails();
    }

    @Override
    protected String getParticipantId(AppUser client) {
        return client.getEmail();
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak().concat(ResUtils.getString(R.string.message_error_client_null));
    }
}
