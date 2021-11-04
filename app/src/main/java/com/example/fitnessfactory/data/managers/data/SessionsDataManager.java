package com.example.fitnessfactory.data.managers.data;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.managers.BaseManager;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.repositories.ownerData.SessionTypeRepository;
import com.example.fitnessfactory.data.repositories.ownerData.participantsData.ClientSessionsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.participantsData.CoachSessionsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.system.SafeReference;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.firebase.firestore.WriteBatch;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class SessionsDataManager extends BaseManager {

    private final SessionsRepository sessionsRepository;
    private final SessionTypeRepository sessionTypeRepository;
    private final ClientSessionsRepository clientSessionsRepository;
    private final CoachSessionsRepository coachSessionsRepository;
    private final OwnerCoachesRepository ownerCoachesRepository;

    @Inject
    public SessionsDataManager(SessionsRepository sessionsRepository,
                               SessionTypeRepository sessionTypeRepository,
                               ClientSessionsRepository clientSessionsRepository,
                               CoachSessionsRepository coachSessionsRepository,
                               OwnerCoachesRepository ownerCoachesRepository) {
        this.sessionsRepository = sessionsRepository;
        this.sessionTypeRepository = sessionTypeRepository;
        this.clientSessionsRepository = clientSessionsRepository;
        this.coachSessionsRepository = coachSessionsRepository;
        this.ownerCoachesRepository = ownerCoachesRepository;
    }

    public Completable addClientToSession(String sessionId, String clientId) {
        SafeReference<Session> sessionRef = new SafeReference<>();

        return sessionsRepository.getSessionAsync(sessionId)
                .flatMap(session -> {
                    sessionRef.set(session);
                    return sessionTypeRepository.getSessionTypeByNameAsync(session.getSessionTypeName());
                })
                .flatMap(sessionType -> sessionsRepository.isSessionPackedAsync(sessionRef.getValue(), sessionType))
                .flatMap(isPacked -> isPacked ?
                        Single.error(new Exception(getSessionPackedMessage())):
                        sessionsRepository.getAddClientBatchAsync(sessionId, clientId))
                .flatMap(writeBatch -> clientSessionsRepository.getAddSessionBatchAsync(writeBatch, sessionId, clientId))
                .flatMapCompletable(this::commitBatchCompletable);
    }

    public Completable addCoachToSession(String sessionId, String coachEmail) {
        SafeReference<String> coachIdRef = new SafeReference<>();

        return ownerCoachesRepository.getPersonnelIdByEmailAsync(coachEmail)
                .flatMap(coachId -> {
                    coachIdRef.set(coachId);
                    return sessionsRepository.getAddCoachBatchAsync(sessionId, coachId);
                })
                .flatMap(writeBatch -> coachSessionsRepository.getAddSessionBatchAsync(writeBatch, sessionId, coachIdRef.getValue()))
                .flatMapCompletable(this::commitBatchCompletable);
    }

    public Completable removeClientFromSession(String sessionId, String clientId) {
        return sessionsRepository.getRemoveClientBatchAsync(sessionId, clientId)
                .flatMap(writeBatch -> clientSessionsRepository.getRemoveSessionBatchAsync(writeBatch, sessionId, clientId))
                .flatMapCompletable(this::commitBatchCompletable);
    }

    public Completable removeCoachFromSession(String sessionId, String coachEmail) {
        SafeReference<String> coachIdRef = new SafeReference<>();

        return ownerCoachesRepository.getPersonnelIdByEmailAsync(coachEmail)
                .flatMap(coachId -> {
                    coachIdRef.set(coachId);
                    return sessionsRepository.getRemoveCoachBatchAsync(sessionId, coachId);
                })
                .flatMap(writeBatch -> coachSessionsRepository.getRemoveSessionBatchAsync(writeBatch, sessionId, coachIdRef.getValue()))
                .flatMapCompletable(this::commitBatchCompletable);
    }

    public Single<Boolean> deleteSessionSingle(Session session) {
        return getDeleteBatch(session)
                .flatMap(this::commitBatchSingle);
    }

    public Completable deleteSessionCompletable(Session session) {
        return getDeleteBatch(session)
                .flatMapCompletable(this::commitBatchCompletable);
    }

    private Single<WriteBatch> getDeleteBatch(Session session) {
        return sessionsRepository.getDeleteBatchAsync(session)
                .flatMap(writeBatch -> clientSessionsRepository.getDeleteSessionBatchAsync(writeBatch, session))
                .flatMap(writeBatch -> coachSessionsRepository.getDeleteSessionBatchAsync(writeBatch, session));
    }

    private String getSessionPackedMessage() {
        return ResUtils.getString(R.string.message_error_session_packed);
    }
}
