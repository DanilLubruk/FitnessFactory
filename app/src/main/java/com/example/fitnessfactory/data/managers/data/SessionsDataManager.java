package com.example.fitnessfactory.data.managers.data;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.managers.BaseManager;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.repositories.SessionViewRepository;
import com.example.fitnessfactory.data.repositories.ownerData.ClientsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerClientsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionTypeRepository;
import com.example.fitnessfactory.data.repositories.ownerData.participantsData.ClientSessionsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.participantsData.CoachSessionsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.data.views.SessionView;
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
    private final SessionViewRepository sessionViewRepository;
    private final OwnerClientsRepository ownerClientsRepository;
    private final ClientsRepository clientsRepository;

    @Inject
    public SessionsDataManager(SessionsRepository sessionsRepository,
                               SessionTypeRepository sessionTypeRepository,
                               ClientSessionsRepository clientSessionsRepository,
                               CoachSessionsRepository coachSessionsRepository,
                               OwnerCoachesRepository ownerCoachesRepository,
                               SessionViewRepository sessionViewRepository,
                               OwnerClientsRepository ownerClientsRepository,
                               ClientsRepository clientsRepository) {
        this.sessionsRepository = sessionsRepository;
        this.sessionTypeRepository = sessionTypeRepository;
        this.clientSessionsRepository = clientSessionsRepository;
        this.coachSessionsRepository = coachSessionsRepository;
        this.ownerCoachesRepository = ownerCoachesRepository;
        this.sessionViewRepository = sessionViewRepository;
        this.ownerClientsRepository = ownerClientsRepository;
        this.clientsRepository = clientsRepository;
    }

    public Single<SessionView> getSessionView(String sessionId) {
        return sessionsRepository.getSessionAsync(sessionId)
                .flatMap(sessionViewRepository::getSessionViewAsync);
    }

    public Completable addClientToSession(String sessionId, String clientEmail) {
        SafeReference<Session> sessionRef = new SafeReference<>();
        SafeReference<WriteBatch> addBatch = new SafeReference<>();

        return sessionsRepository.getSessionAsync(sessionId)
                .flatMap(session -> {
                    sessionRef.set(session);
                    return sessionTypeRepository.getSessionTypeByIdAsync(session.getSessionTypeId());
                })
                .flatMap(sessionType -> sessionsRepository.isSessionPackedAsync(sessionRef.getValue(), sessionType))
                .flatMap(isPacked -> isPacked ?
                        Single.error(new Exception(getSessionPackedMessage())) :
                        sessionsRepository.getAddClientBatchAsync(sessionId, clientEmail))
                .flatMap(writeBatch -> {
                    addBatch.set(writeBatch);
                    return clientsRepository.getClientIdAsync(clientEmail);
                })
                .flatMap(clientId -> clientSessionsRepository.getAddSessionBatchAsync(addBatch.getValue(), sessionId, clientId))
                .flatMapCompletable(this::commitBatchCompletable);
    }

    public Completable addCoachToSession(String sessionId, String coachEmail) {
        SafeReference<String> coachIdRef = new SafeReference<>();

        return ownerCoachesRepository.getPersonnelIdByEmailAsync(coachEmail)
                .flatMap(coachId -> {
                    coachIdRef.set(coachId);
                    return ownerCoachesRepository.getPersonnelGymsIdsByEmailAsync(coachEmail);
                })
                .flatMap(coachGymsIds -> sessionsRepository.doesCoachWorkAtSessionsGymAsync(sessionId, coachGymsIds))
                .flatMap(doesCoachWork -> doesCoachWork ?
                        coachSessionsRepository.getParticipantSessionsIdsAsync(coachIdRef.getValue()) :
                        Single.error(new Exception(getCoachNotHaveGym())))
                .flatMap(sessionsIds -> sessionsRepository.doesSessionsTimeIntersectWithAnyAsync(sessionId, sessionsIds))
                .flatMap(doesIntersect -> doesIntersect ?
                        Single.error(new Exception(getCoachOccupiedMessage()))
                        : sessionsRepository.getAddCoachBatchAsync(sessionId, coachEmail))
                .flatMap(writeBatch -> coachSessionsRepository.getAddSessionBatchAsync(writeBatch, sessionId, coachIdRef.getValue()))
                .flatMapCompletable(this::commitBatchCompletable);
    }

    public Completable removeClientFromSession(String sessionId, String clientEmail) {
        SafeReference<String> clientIdRef = new SafeReference<>();

        return ownerClientsRepository.getPersonnelIdByEmailAsync(clientEmail)
                .flatMap(clientId -> {
                    clientIdRef.set(clientId);
                    return sessionsRepository.getRemoveClientBatchAsync(sessionId, clientEmail);
                })
                .flatMap(writeBatch -> clientSessionsRepository.getRemoveSessionBatchAsync(writeBatch, sessionId, clientIdRef.getValue()))
                .flatMapCompletable(this::commitBatchCompletable);
    }

    public Completable removeCoachFromSession(String sessionId, String coachEmail) {
        SafeReference<String> coachIdRef = new SafeReference<>();

        return ownerCoachesRepository.getPersonnelIdByEmailAsync(coachEmail)
                .flatMap(coachId -> {
                    coachIdRef.set(coachId);
                    return sessionsRepository.getRemoveCoachBatchAsync(sessionId, coachEmail);
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

    private String getCoachOccupiedMessage() {
        return ResUtils.getString(R.string.message_error_coach_occupied);
    }

    private String getCoachNotHaveGym() {
        return ResUtils.getString(R.string.message_error_coach_not_have_gym);
    }
}
