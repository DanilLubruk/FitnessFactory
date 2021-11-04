package com.example.fitnessfactory.data.managers.data;

import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class CoachesDataManager extends PersonnelDataManager {

    @Inject
    public CoachesDataManager(OwnerCoachesRepository ownerRepository,
                              UserRepository userRepository,
                              OwnerGymRepository gymRepository) {
        super(ownerRepository, userRepository, gymRepository);
    }

    public Single<List<AppUser>> getCoachesUsers(List<String> coachesIds) {
        return getOwnerRepository().getPersonnelEmailsAsync(coachesIds)
                .flatMap(getUserRepository()::getUsersByEmailsAsync);
    }
}
