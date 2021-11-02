package com.example.fitnessfactory.data.managers.data;

import com.example.fitnessfactory.data.managers.BaseManager;
import com.example.fitnessfactory.data.repositories.ownerData.ClientsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.system.SafeReference;

import javax.inject.Inject;

import io.reactivex.Completable;

public class SessionsDataManager extends BaseManager {

    private final SessionsRepository sessionsRepository;
    private final ClientsRepository clientsRepository;
    private final OwnerCoachesRepository ownerCoachesRepository;

    @Inject
    public SessionsDataManager(SessionsRepository sessionsRepository,
                               ClientsRepository clientsRepository,
                               OwnerCoachesRepository ownerCoachesRepository) {
        this.sessionsRepository = sessionsRepository;
        this.clientsRepository = clientsRepository;
        this.ownerCoachesRepository = ownerCoachesRepository;
    }

    public Completable addClientToSession(String sessionId, String clientId) {
        return sessionsRepository.getAddClientBatchAsync(sessionId, clientId)
                .flatMap(writeBatch -> clientsRepository.getAddSessionBatchAsync(writeBatch, sessionId, clientId))
                .flatMapCompletable(this::commitBatchCompletable);
    }

    public Completable addCoachToSession(String sessionId, String coachEmail) {
        SafeReference<String> coachIdRef = new SafeReference<>();

        return ownerCoachesRepository.getCoachIdByEmailAsync(coachEmail)
                .flatMap(coachId -> {
                    coachIdRef.set(coachId);
                    return sessionsRepository.getAddCoachBatchAsync(sessionId, coachId);
                })
                .flatMap(writeBatch -> ownerCoachesRepository.getAddSessionBatchAsync(writeBatch, sessionId, coachIdRef.getValue()))
                .flatMapCompletable(this::commitBatchCompletable);
    }

    public Completable removeClientFromSession(String sessionId, String clientId) {
        return sessionsRepository.getRemoveClientBatchAsync(sessionId, clientId)
                .flatMap(writeBatch -> clientsRepository.getRemoveSessionBatchAsync(writeBatch, sessionId, clientId))
                .flatMapCompletable(this::commitBatchCompletable);
    }

    public Completable removeCoachFromSession(String sessionId, String coachEmail) {
        SafeReference<String> coachIdRef = new SafeReference<>();

        return ownerCoachesRepository.getCoachIdByEmailAsync(coachEmail)
                .flatMap(coachId -> {
                    coachIdRef.set(coachId);
                    return sessionsRepository.getRemoveCoachBatchAsync(sessionId, coachId);
                })
                .flatMap(writeBatch -> ownerCoachesRepository.getRemoveSessionBatchAsync(writeBatch, sessionId, coachIdRef.getValue()))
                .flatMapCompletable(this::commitBatchCompletable);
    }
}
