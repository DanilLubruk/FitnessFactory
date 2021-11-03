package com.example.fitnessfactory.data.managers.data;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.managers.BaseManager;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.data.repositories.ownerData.ClientsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.participantsData.ClientSessionsRepository;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class ClientsDataManager extends BaseManager {

    private final ClientsRepository clientsRepository;
    private final ClientSessionsRepository clientSessionsRepository;

    @Inject
    public ClientsDataManager(ClientsRepository clientsRepository,
                              ClientSessionsRepository clientSessionsRepository) {
        this.clientsRepository = clientsRepository;
        this.clientSessionsRepository = clientSessionsRepository;
    }

    public Single<Boolean> deleteClientSingle(Client client) {
        return clientSessionsRepository.isParticipantOccupiedAsync(client.getId())
                .flatMap(isOccupied -> isOccupied ?
                        Single.error(new Exception(getOccupiedMessage()))
                        : clientsRepository.deleteClientSingle(client));
    }

    public Completable deleteClientCompletable(Client client) {
        return clientSessionsRepository.isParticipantOccupiedAsync(client.getId())
                .flatMapCompletable(isOccupied -> isOccupied ?
                        Completable.error(new Exception(getOccupiedMessage()))
                        : clientsRepository.deleteClientCompletable(client));
    }

    private String getOccupiedMessage() {
        return String.format(
                ResUtils.getString(R.string.message_error_item_occupied),
                ResUtils.getString(R.string.caption_client));
    }
}
