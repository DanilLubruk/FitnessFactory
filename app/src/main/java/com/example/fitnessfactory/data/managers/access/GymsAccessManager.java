package com.example.fitnessfactory.data.managers.access;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.managers.BaseManager;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.firebase.firestore.WriteBatch;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class GymsAccessManager extends BaseManager {

    private final SessionsRepository sessionsRepository;
    private final OwnerGymRepository ownerGymRepository;
    private final OwnerAdminsRepository ownerAdminsRepository;
    private final OwnerCoachesRepository ownerCoachesRepository;

    @Inject
    public GymsAccessManager(SessionsRepository sessionsRepository,
                             OwnerGymRepository ownerGymRepository,
                             OwnerAdminsRepository ownerAdminsRepository,
                             OwnerCoachesRepository ownerCoachesRepository) {
        this.sessionsRepository = sessionsRepository;
        this.ownerGymRepository = ownerGymRepository;
        this.ownerAdminsRepository = ownerAdminsRepository;
        this.ownerCoachesRepository = ownerCoachesRepository;
    }

    public Single<Boolean> deleteGymSingle(Gym gym) {
        return getDeleteBatch(gym)
                .flatMap(this::commitBatchSingle);
    }

    public Completable deleteGymCompletable(Gym gym) {
        return getDeleteBatch(gym)
                .flatMapCompletable(this::commitBatchCompletable);
    }

    private Single<WriteBatch> getDeleteBatch(Gym gym) {
        return sessionsRepository.isGymNameOccupiedAsync(gym.getName())
                .flatMap(isOccupied -> isOccupied ?
                        Single.error(new Exception(getOccupiedMessage()))
                        : ownerGymRepository.getDeleteGymBatchAsync(gym.getId()))
                .flatMap(deleteBatch ->
                        ownerAdminsRepository.getRemoveGymFromPersonnelBatchAsync(deleteBatch, gym.getId()))
                .flatMap(deleteBatch ->
                        ownerCoachesRepository.getRemoveGymFromPersonnelBatchAsync(deleteBatch, gym.getId()));
    }

    private String getOccupiedMessage() {
        return String.format(
                ResUtils.getString(R.string.message_error_item_occupied),
                ResUtils.getString(R.string.caption_gym));
    }
}
