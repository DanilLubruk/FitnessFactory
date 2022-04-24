package com.example.fitnessfactory.data.managers.data;

import com.example.fitnessfactory.data.managers.BaseManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;

import java.util.List;

import io.reactivex.Single;

public abstract class PersonnelDataManager extends BaseManager {

    private OwnerPersonnelRepository ownerRepository;

    private UserRepository userRepository;

    private OwnerGymRepository gymRepository;

    public PersonnelDataManager(OwnerPersonnelRepository ownerRepository,
                                UserRepository userRepository,
                                OwnerGymRepository gymRepository) {
        this.ownerRepository = ownerRepository;
        this.userRepository = userRepository;
        this.gymRepository = gymRepository;
    }

    protected OwnerPersonnelRepository getOwnerRepository() {
        return ownerRepository;
    }

    protected UserRepository getUserRepository() {
        return userRepository;
    }

    protected OwnerGymRepository getGymRepository() {
        return gymRepository;
    }

    public Single<List<AppUser>> getPersonnelListAsync() {
        return getOwnerRepository().getPersonnelIdsAsync()
                .flatMap(getUserRepository()::getUsersByIdsAsync);
    }

    public Single<List<AppUser>> getPersonnelListByGymIdAsync(String gymId) {
        return getOwnerRepository().getPersonnelIdsByGymIdAsync(gymId)
                .flatMap(getUserRepository()::getUsersByIdsAsync);
    }

    public Single<List<Gym>> getPersonnelGymsById(String personnelId) {
        return getOwnerRepository().getPersonnelGymsIdsByIdAsync(personnelId)
                .flatMap(getGymRepository()::getGymsByIdsAsync);
    }
}
