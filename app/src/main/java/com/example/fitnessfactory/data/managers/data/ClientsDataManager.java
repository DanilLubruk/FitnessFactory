package com.example.fitnessfactory.data.managers.data;

import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerClientsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.data.repositories.ownerData.participantsData.ClientSessionsRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class ClientsDataManager extends PersonnelDataManager {

    private final ClientSessionsRepository clientSessionsRepository;

    @Inject
    public ClientsDataManager(OwnerClientsRepository clientsRepository,
                              ClientSessionsRepository clientSessionsRepository,
                              UserRepository userRepository,
                              OwnerGymRepository gymRepository) {
        super(clientsRepository, userRepository, gymRepository);
        this.clientSessionsRepository = clientSessionsRepository;
    }

    /*public Single<Boolean> deleteClientSingle(Client client) {
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
    }*/
}
