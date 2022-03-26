package com.example.fitnessfactory.data.managers.data;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.managers.BaseManager;
import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.data.repositories.ownerData.SessionTypeRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class SessionTypesDataManager extends BaseManager {

    private final SessionsRepository sessionsRepository;
    private final SessionTypeRepository sessionTypeRepository;

    @Inject
    public SessionTypesDataManager(SessionsRepository sessionsRepository,
                                   SessionTypeRepository sessionTypeRepository) {
        this.sessionsRepository = sessionsRepository;
        this.sessionTypeRepository = sessionTypeRepository;
    }

    public Completable deleteSessionTypeCompletable(SessionType sessionType) {
        return sessionsRepository.isSessionTypeOccupiedAsync(sessionType.getId())
                .flatMapCompletable(isOccupied -> isOccupied ?
                        Completable.error(new Exception(getOccupiedMessage()))
                        :
                        sessionTypeRepository.deleteSessionTypeCompletable(sessionType));
    }

    public Single<Boolean> deleteSessionTypeSingle(SessionType sessionType) {
        return sessionsRepository.isSessionTypeOccupiedAsync(sessionType.getId())
                .flatMap(isOccupied -> isOccupied ?
                        Single.error(new Exception(getOccupiedMessage()))
                        :
                        sessionTypeRepository.deleteSessionTypeSingle(sessionType));
    }

    private String getOccupiedMessage() {
        return String.format(
                ResUtils.getString(R.string.message_error_item_occupied),
                ResUtils.getString(R.string.caption_session_type));
    }
}
