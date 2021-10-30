package com.example.fitnessfactory.data.managers.data;

import com.example.fitnessfactory.data.managers.BaseManager;
import com.example.fitnessfactory.data.repositories.ownerData.ClientsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;

import javax.inject.Inject;

import io.reactivex.Completable;

public class SessionsDataManager extends BaseManager {

    private final SessionsRepository sessionsRepository;
    private final ClientsRepository clientsRepository;

    @Inject
    public SessionsDataManager(SessionsRepository sessionsRepository,
                               ClientsRepository clientsRepository) {
        this.sessionsRepository = sessionsRepository;
        this.clientsRepository = clientsRepository;
    }

    public Completable addClientToSession(String sessionId, String clientId) {
        return sessionsRepository.getAddClientBatchAsync(sessionId, clientId)
                .flatMap(writeBatch -> clientsRepository.getAddSessionBatchAsync(writeBatch, sessionId, clientId))
                .flatMapCompletable(this::commitBatchCompletable);
    }
}
