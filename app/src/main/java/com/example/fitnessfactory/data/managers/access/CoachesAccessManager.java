package com.example.fitnessfactory.data.managers.access;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.access.CoachesAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.participantsData.CoachSessionsRepository;
import com.example.fitnessfactory.system.SafeReference;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class CoachesAccessManager extends PersonnelAccessManager {

    private final CoachSessionsRepository coachSessionsRepository;

    @Inject
    public CoachesAccessManager(CoachSessionsRepository coachSessionsRepository,
                                CoachesAccessRepository accessRepository,
                                OwnerCoachesRepository ownerRepository,
                                UserRepository userRepository) {
        super(accessRepository, ownerRepository, userRepository);
        this.coachSessionsRepository = coachSessionsRepository;
    }

    @Override
    public Single<Boolean> deletePersonnelSingle(String ownerId, String userId) {
        return ownerRepository.isPersonnelOccupiedWithGyms(userId)
                .flatMap(isOccupiedWithGyms -> isOccupiedWithGyms ?
                        Single.error(new Exception(getOccupiedMessage())) :
                        coachSessionsRepository.isParticipantOccupiedAsync(userId))
                .flatMap(isOccupied -> isOccupied ?
                        Single.error(new Exception(getOccupiedMessage()))
                        : super.deletePersonnelSingle(ownerId, userId));
    }

    public Completable deletePersonnelCompletable(String ownerId, String userId) {
        return ownerRepository.isPersonnelOccupiedWithGyms(userId)
                .flatMap(isOccupiedWithGyms -> isOccupiedWithGyms ?
                        Single.error(new Exception(getOccupiedMessage())) :
                        coachSessionsRepository.isParticipantOccupiedAsync(userId))
                .flatMapCompletable(isOccupied -> isOccupied ?
                        Completable.error(new Exception(getOccupiedMessage()))
                        : super.deletePersonnelCompletable(ownerId, userId));
    }

    private String getOccupiedMessage() {
        return String.format(
                ResUtils.getString(R.string.message_error_item_occupied),
                ResUtils.getString(R.string.caption_coach_capitalized));
    }

    @Override
    protected String getAlreadyRegisteredMessage() {
        return ResUtils.getString(R.string.message_coach_is_registered);
    }
}
