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
}
