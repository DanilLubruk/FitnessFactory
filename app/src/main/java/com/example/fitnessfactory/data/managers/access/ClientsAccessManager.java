package com.example.fitnessfactory.data.managers.access;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.access.ClientsAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerClientsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.participantsData.ClientSessionsRepository;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class ClientsAccessManager extends PersonnelAccessManager {

    private ClientSessionsRepository clientSessionsRepository;

    @Inject
    public ClientsAccessManager(ClientsAccessRepository accessRepository,
                                OwnerClientsRepository ownerRepository,
                                ClientSessionsRepository clientSessionsRepository,
                                UserRepository userRepository) {
        super(accessRepository, ownerRepository, userRepository);
        this.clientSessionsRepository = clientSessionsRepository;
    }

    @Override
    public Single<Boolean> deletePersonnelSingle(String ownerId, String userId) {
        return
                clientSessionsRepository.isParticipantOccupiedAsync(userId)
                .flatMap(isOccupied -> isOccupied ?
                        Single.error(new Exception(getOccupiedMessage()))
                        : super.deletePersonnelSingle(ownerId, userId));
    }

    public Completable deletePersonnelCompletable(String ownerId, String userId) {
        return clientSessionsRepository.isParticipantOccupiedAsync(userId)
                .flatMapCompletable(isOccupied -> isOccupied ?
                        Completable.error(new Exception(getOccupiedMessage()))
                        : super.deletePersonnelCompletable(ownerId, userId));
    }

    private String getOccupiedMessage() {
        return String.format(
                ResUtils.getString(R.string.message_error_item_occupied),
                ResUtils.getString(R.string.caption_client_capitalized));
    }

    @Override
    protected String getAlreadyRegisteredMessage() {
        return ResUtils.getString(R.string.message_client_is_registered);
    }
}
