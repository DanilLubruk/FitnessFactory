package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.firestoreCollections.ClientsCollection;
import com.example.fitnessfactory.data.firestoreCollections.ClientsSessionsCollection;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.data.models.UsersSession;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.Single;

public class ClientsRepository extends BaseRepository {

    @Override
    protected String getRoot() {
        return ClientsCollection.getRoot();
    }

    public Single<List<String>> getClientsEmailsAsync(List<String> clientsIds) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getClientsEmails(clientsIds));
            }
        });
    }

    private List<String> getClientsEmails(List<String> clientsIds) throws ExecutionException, InterruptedException {
        List<String> emails = new ArrayList<>();

        for (Client client : getClients(clientsIds)) {
            if (client != null) {
                emails.add(client.getEmail());
            }
        }

        return emails;
    }

    public Single<List<Client>> getClientsAsync(List<String> clientsIds) {
        return SingleCreate(emitter -> {
           if (!emitter.isDisposed()) {
               emitter.onSuccess(getClients(clientsIds));
           }
        });
    }

    private List<Client> getClients(List<String> clientsIds) throws ExecutionException, InterruptedException {
        if (clientsIds == null || clientsIds.isEmpty()) {
            return new ArrayList<>();
        }

        return Tasks.await(newQuery().whereIdInArray(clientsIds).build().get()).toObjects(Client.class);
    }

    public Single<Boolean> saveAsync(Client client) {
        return SingleCreate(emitter -> {
            boolean isSaved = save(client);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isSaved);
            }
        });
    }

    private boolean save(Client client) throws Exception {
        if (client == null) {
            throw new Exception(getEntitySavingNullMessage());
        }
        boolean isNewClient = StringUtils.isEmpty(client.getId());

        return isNewClient ? insert(client) : update(client);
    }

    private boolean insert(Client client) throws Exception {
        if (isNewClientsEmailOccupied(client)) {
            throw new Exception(getEmailOccupiedMessage());
        }

        DocumentReference documentReference = getCollection().document();
        client.setId(documentReference.getId());
        Tasks.await(documentReference.set(client));

        return true;
    }

    private boolean isNewClientsEmailOccupied(Client client) throws Exception {
        int clientsWithEmail =
                getEntitiesAmount(newQuery()
                        .whereEmailEquals(client.getEmail())
                        .build());

        return clientsWithEmail > 0;
    }

    private boolean update(Client client) throws Exception {
        if (isExistingClientsEmailOccupied(client)) {
            throw new Exception(getEmailOccupiedMessage());
        }

        Tasks.await(getCollection().document(client.getId()).set(client));

        return true;
    }

    private boolean isExistingClientsEmailOccupied(Client client) throws Exception {
        int clientsWithEmail =
                getEntitiesAmount(newQuery()
                        .whereIdNotEquals(client.getId())
                        .whereEmailEquals(client.getEmail())
                        .build());

        return clientsWithEmail > 0;
    }


    public Single<Client> getClientAsync(String clientId) {
        return SingleCreate(emitter -> {
            Client client = getClient(clientId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(client);
            }
        });
    }

    private Client getClient(String clientId) throws Exception {
        if (StringUtils.isEmpty(clientId)) {
            return new Client();
        }

        return getUniqueClientEntity(clientId);
    }

    public Single<Boolean> deleteClientSingle(Client client) {
        return SingleCreate(emitter -> {
            boolean isDeleted = deleteClient(client);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isDeleted);
            }
        });
    }

    public Completable deleteClientCompletable(Client client) {
        return CompletableCreate(emitter -> {
            deleteClient(client);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private boolean deleteClient(Client client) throws Exception {
        Tasks.await(getUniqueClientDocument(client.getId()).delete());

        return true;
    }

    private Client getUniqueClientEntity(String clientId) throws Exception {
        return getUniqueClientSnapshot(clientId).toObject(Client.class);
    }

    private DocumentReference getUniqueClientDocument(String clientId) throws Exception {
        return getUniqueClientSnapshot(clientId).getReference();
    }

    private DocumentSnapshot getUniqueClientSnapshot(String clientId) throws Exception {
        return getUniqueEntitySnapshot(
                newQuery().whereIdEquals(clientId).build(),
                getClientNotUniqueMessage());
    }

    private String getClientNotUniqueMessage() {
        return ResUtils.getString(R.string.message_client_not_unique);
    }

    private String getEmailOccupiedMessage() {
        return ResUtils.getString(R.string.message_email_occupied);
    }

    @Override
    protected String getEntitySavingNullMessage() {
        return super.getEntitySavingNullMessage()
                .concat(ResUtils.getString(R.string.message_error_client_null));
    }

    private ClientsRepository.QueryBuilder newQuery() {
        return new ClientsRepository().new QueryBuilder();
    }

    private class QueryBuilder {

        private Query query = getCollection();

        public QueryBuilder whereIdEquals(String clientId) {
            query = query.whereEqualTo(Client.ID_FIELD, clientId);
            return this;
        }

        public QueryBuilder whereIdNotEquals(String clientId) {
            query = query.whereNotEqualTo(Client.ID_FIELD, clientId);
            return this;
        }

        public QueryBuilder whereEmailEquals(String clientEmail) {
            query = query.whereEqualTo(Client.EMAIL_FIELD, clientEmail);
            return this;
        }

        public QueryBuilder whereIdInArray(List<String> clientsIds) {
            query = query.whereIn(Client.ID_FIELD, clientsIds);
            return this;
        }

        public Query build() {
            return query;
        }
    }
}
