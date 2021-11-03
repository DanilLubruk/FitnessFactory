package com.example.fitnessfactory.ui.viewmodels.editors;

import android.os.Bundle;

import androidx.databinding.ObservableField;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.ownerData.ClientsRepository;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class ClientEditorViewModel extends EditorViewModel {

    private ClientsRepository clientsRepository;
    public ObservableField<Client> client = new ObservableField<>();
    private Client dbClient;

    private final String DB_ID_KEY = "DB_ID_KEY";
    private final String DB_NAME_KEY = "DB_NAME_KEY";
    private final String DB_EMAIL_KEY = "DB_EMAIL_KEY";

    @Inject
    public ClientEditorViewModel(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    public void getClientData(String clientId) {
        subscribeInIOThread(
                clientsRepository.getClientAsync(clientId),
                new SingleData<>(
                        this::setClient,
                        getErrorHandler()::handleError));
    }

    private void setClient(Client client) {
        if (client == null) {
            client = new Client();
        }
        if (dbClient == null) {
            dbClient = new Client();
            dbClient.copy(client);
        }
        if (hasHandle()) {
            setHandleState(client);
            setDbHandleState();
        }

        this.client.set(client);
    }

    @Override
    public SingleLiveEvent<Boolean> isModified() {
        SingleLiveEvent<Boolean> isModified = new SingleLiveEvent<>();

        Client client = this.client.get();
        if (Client.isNotNull(client)) {
            isModified.setValue(!client.equals(dbClient));
        } else {
            isModified.setValue(false);
        }

        return isModified;
    }

    @Override
    public SingleLiveEvent<Boolean> save() {
        SingleLiveEvent<Boolean> isSaved = new SingleLiveEvent<>();

        Client client = this.client.get();
        if (client == null) {
            return handleItemSavingNullError(isSaved);
        }

        subscribeInIOThread(
                clientsRepository.saveAsync(client),
                new SingleData<>(
                        isSavedResult -> {
                            dbClient.copy(client);
                            isSaved.setValue(isSavedResult);
                        },
                        throwable -> getErrorHandler().handleError(isSaved, throwable)));

        return isSaved;
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak()
                .concat(ResUtils.getString(R.string.message_error_client_null));
    }

    @Override
    public SingleLiveEvent<Boolean> delete() {
        SingleLiveEvent<Boolean> isDeleted = new SingleLiveEvent<>();

        Client client = this.client.get();
        if (client == null) {
            isDeleted.setValue(false);
            return isDeleted;
        }

        subscribeInIOThread(
                clientsRepository.deleteClientSingle(client),
                new SingleData<>(
                        isDeleted::setValue,
                        throwable -> getErrorHandler().handleError(isDeleted, throwable)));

        return isDeleted;
    }

    @Override
    public void saveState(Bundle savedStated) {
        super.saveState(savedStated);
        saveClientState();
        saveDbClientState();
    }

    private void saveClientState() {
        Client client = this.client.get();
        if (client == null) {
            return;
        }

        getHandle().put(Client.ID_FIELD, client.getId());
        getHandle().put(Client.NAME_FIELD, client.getName());
        getHandle().put(Client.EMAIL_FIELD, client.getEmail());
    }

    private void saveDbClientState() {
        if (dbClient == null) {
            return;
        }

        getHandle().put(DB_ID_KEY, dbClient.getId());
        getHandle().put(DB_NAME_KEY, dbClient.getName());
        getHandle().put(DB_EMAIL_KEY, dbClient.getEmail());
    }

    @Override
    public void restoreState(Bundle savedState) {
        super.restoreState(savedState);
    }

    private void setHandleState(Client client) {
        if (client == null) {
            client = new Client();
        }

        client.setId((String) getHandle().get(Client.ID_FIELD));
        client.setName((String) getHandle().get(Client.NAME_FIELD));
        client.setEmail((String) getHandle().get(Client.EMAIL_FIELD));
    }

    private void setDbHandleState() {
        if (dbClient == null) {
            dbClient = new Client();
        }

        dbClient.setId((String) getHandle().get(DB_ID_KEY));
        dbClient.setName((String) getHandle().get(DB_NAME_KEY));
        dbClient.setEmail((String) getHandle().get(DB_EMAIL_KEY));
    }
}
