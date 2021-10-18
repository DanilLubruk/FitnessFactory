package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.firestoreCollections.ClientsCollection;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class ClientsRepository extends BaseRepository {

    @Override
    public String getRoot() {
        return ClientsCollection.getRoot();
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
            throw new Exception(getClientNullMessage());
        }
        boolean isNewClient = StringUtils.isEmpty(client.getId());

        return isNewClient ? insert(client) : update(client);
    }

    private boolean insert(Client client) throws Exception {
        if (isNewClientsEmailAvailable(client)) {
            throw new Exception(ResUtils.getString(R.string.message_email_occupied));
        }

        DocumentReference documentReference = getCollection().document();
        client.setId(documentReference.getId());
        Tasks.await(documentReference.set(client));

        return true;
    }

    private boolean update(Client client) throws Exception {
        if (isExistingClientsEmailAvailable(client)) {
            throw new Exception(ResUtils.getString(R.string.message_email_occupied));
        }

        DocumentReference documentReference =
                getUniqueUserEntityReference(
                        getCollection().whereEqualTo(Client.EMAIL_FIELD,
                                client.getEmail()));

        Tasks.await(documentReference.set(client));

        return true;
    }

    private boolean isNewClientsEmailAvailable(Client client) throws Exception {
        int clientsWithEmail = getClientsWithEmailAmount(client.getEmail());

        return clientsWithEmail == 0;
    }

    private boolean isExistingClientsEmailAvailable(Client client) throws Exception {
        int clientsWithEmail = getClientsWithEmailAmount(client.getId(), client.getEmail());

        return clientsWithEmail == 0;
    }

    private int getClientsWithEmailAmount(String clientId, String clientEmail) throws Exception {
        return getClientsAmount(
                ClientsRepository.newBuilder()
                        .whereIdNotEquals(clientId)
                        .whereEmailEquals(clientEmail)
                        .build());
    }

    private int getClientsWithEmailAmount(String clientEmail) throws Exception {
        return getClientsAmount(
                        ClientsRepository.newBuilder()
                                .whereEmailEquals(clientEmail)
                                .build());
    }

    private int getClientsAmount(Query query) throws Exception {
        List<Client> clients =
                Tasks.await(
                query.get())
                .toObjects(Client.class);
        checkEmailUniqueness(clients);

        return clients.size();
    }

    private String getClientNullMessage() {
        return ResUtils.getString(R.string.message_error_data_save)
                .concat(" - ")
                .concat(ResUtils.getString(R.string.message_error_client_null));
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

        return getUniqueUserEntity(
                getCollection().whereEqualTo(Client.ID_FIELD, clientId),
                Client.class);
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
        Tasks.await(getUniqueEntityReference(
                getCollection().whereEqualTo(Client.ID_FIELD, client.getId()),
                ResUtils.getString(R.string.message_client_not_unique))
                .delete());

        return true;
    }

    public static ClientsRepository.QueryBuilder newBuilder() {
        return new ClientsRepository().new QueryBuilder();
    }

    public class QueryBuilder {

        private Query query = getCollection();

        public QueryBuilder whereIdNotEquals(String clientId) {
            query = query.whereNotEqualTo(Client.ID_FIELD, clientId);
            return this;
        }

        public QueryBuilder whereEmailEquals(String clientEmail) {
            query = query.whereEqualTo(Client.EMAIL_FIELD, clientEmail);
            return this;
        }

        public Query build() {
            return query;
        }
    }
}
