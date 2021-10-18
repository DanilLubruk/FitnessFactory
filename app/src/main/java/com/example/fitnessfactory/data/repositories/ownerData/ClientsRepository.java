package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.firestoreCollections.ClientsCollection;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

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
        if (isNewClientsEmailOccupied(client)) {
            throw new Exception(getEmailOccupiedMessage());
        }

        DocumentReference documentReference = getCollection().document();
        client.setId(documentReference.getId());
        Tasks.await(documentReference.set(client));

        return true;
    }

    private boolean update(Client client) throws Exception {
        if (isExistingClientsEmailOccupied(client)) {
            throw new Exception(getEmailOccupiedMessage());
        }

        Tasks.await(getCollection().document(client.getId()).set(client));

        return true;
    }

    private boolean isNewClientsEmailOccupied(Client client) throws Exception {
        int clientsWithEmail =
                getEntitiesAmount(newQuery()
                        .whereEmailEquals(client.getEmail())
                        .build());

        return clientsWithEmail > 0;
    }

    private boolean isExistingClientsEmailOccupied(Client client) throws Exception {
        int clientsWithEmail =
                getEntitiesAmount(newQuery()
                        .whereIdNotEquals(client.getId())
                        .whereEmailEquals(client.getEmail())
                        .build());

        return clientsWithEmail > 0;
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
        Tasks.await(getUniqueClientDocument(client.getId())
                .delete());

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
                getCollection().whereEqualTo(Client.ID_FIELD, clientId),
                getClientNotUniqueMessage());
    }

    private String getClientNotUniqueMessage() {
        return ResUtils.getString(R.string.message_client_not_unique);
    }

    private String getEmailOccupiedMessage() {
        return ResUtils.getString(R.string.message_email_occupied);
    }

    private ClientsRepository.QueryBuilder newQuery() {
        return new ClientsRepository().new QueryBuilder();
    }

    private class QueryBuilder {

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
