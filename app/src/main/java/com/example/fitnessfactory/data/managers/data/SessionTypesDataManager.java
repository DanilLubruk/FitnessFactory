package com.example.fitnessfactory.data.managers.data;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.managers.BaseManager;
import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.data.repositories.ownerData.SessionTypeRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.utils.ResUtils;

import io.reactivex.Completable;
import io.reactivex.Single;

public class SessionTypesDataManager extends BaseManager {

    private final SessionsRepository sessionsRepository;
    private final SessionTypeRepository sessionTypeRepository;

    public SessionTypesDataManager(SessionsRepository sessionsRepository,
                                   SessionTypeRepository sessionTypeRepository) {
        this.sessionsRepository = sessionsRepository;
        this.sessionTypeRepository = sessionTypeRepository;
    }

    public Completable deleteSessionTypeCompletable(SessionType sessionType) {
        return sessionsRepository.isSessionTypeOccupiedAsync(sessionType.getName())
                .flatMapCompletable(isOccupied -> isOccupied ?
                        Completable.error(new Exception(ResUtils.getString(R.string.message_error_session_type_occupied)))
                        :
                        sessionTypeRepository.deleteSessionTypeCompletable(sessionType));
    }

    public Single<Boolean> deleteSessionTypeSingle(SessionType sessionType) {
        return sessionsRepository.isSessionTypeOccupiedAsync(sessionType.getName())
                .flatMap(isOccupied -> isOccupied ?
                        Single.error(new Exception(ResUtils.getString(R.string.message_error_session_type_occupied)))
                        :
                        sessionTypeRepository.deleteSessionTypeSingle(sessionType));
    }
}
