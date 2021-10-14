package com.example.fitnessfactory.ui.viewmodels.editors;

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

    @Inject
    public ClientEditorViewModel(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    public void getClientData(String clientEmail) {
        subscribeInIOThread(
                clientsRepository.getClientAsync(clientEmail),
                new SingleData<>(
                        this::setClient,
                        getErrorHandler()::handleError));
    }

    private void setClient(Client client) {
        if (client == null) {
            handleItemObtainingNullError();
            return;
        }
        if (dbClient == null) {
            dbClient = new Client();
            dbClient.copy(client);
        }
        if (hasHandle()) {
            //restoreState - db and data;
        }

        this.client.set(client);
    }

    @Override
    public SingleLiveEvent<Boolean> isModified() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();
        observer.setValue(false);

        Client client = this.client.get();
        if (client != null &&
                client.getName() != null &&
                client.getEmail() != null) {
            boolean isModified = !client.equals(dbClient);

            observer.setValue(isModified);
        }

        return observer;
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
                        getErrorHandler()::handleError));

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
}
