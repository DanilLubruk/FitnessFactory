package com.example.fitnessfactory.data.repositories.ownerData;

import android.text.TextUtils;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.firestoreCollections.ClientsCollection;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

        return isNewClient(client.getEmail()) ? insert(client) : update(client);
    }

    private boolean insert(Client client) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = getCollection().document();
        Tasks.await(documentReference.set(client));

        return true;
    }

    private boolean update(Client client) throws Exception {
        DocumentReference documentReference =
                getUniqueUserEntityReference(
                        getCollection().whereEqualTo(Client.EMAIL_FIELD,
                                client.getEmail()));

        Tasks.await(documentReference.set(client));

        return true;
    }

    private boolean isNewClient(String clientEmail) throws Exception {
        int clientsWithEmail = getClientsNumberWithThisEmail(clientEmail);

        return clientsWithEmail == 0;
    }

    private int getClientsNumberWithThisEmail(String clientEmail) throws Exception {
        List<Client> clients =
                Tasks.await(
                getCollection().whereEqualTo(Client.EMAIL_FIELD, clientEmail).get())
                .toObjects(Client.class);
        checkEmailUniqueness(clients);

        return clients.size();
    }

    private String getClientNullMessage() {
        return ResUtils.getString(R.string.message_error_data_save)
                .concat(" - ")
                .concat(ResUtils.getString(R.string.message_error_client_null));
    }

    public Single<Client> getClientAsync(String clientEmail) {
        return SingleCreate(emitter -> {
            Client client = getClient(clientEmail);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(client);
            }
        });
    }

    private Client getClient(String clientEmail) throws Exception {
        if (TextUtils.isEmpty(clientEmail)) {
            return new Client();
        }

        return getUniqueUserEntity(
                getCollection().whereEqualTo(Client.EMAIL_FIELD, clientEmail),
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
        Tasks.await(getUniqueUserEntityReference(
                getCollection().whereEqualTo(Client.EMAIL_FIELD, client.getEmail()))
                .delete());

        return true;
    }
}
