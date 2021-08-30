package com.example.fitnessfactory.data.managers;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.OwnerGymRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class GymsDataManager extends BaseManager {

    @Inject
    OwnerAdminsRepository ownerAdminsRepository;
    @Inject
    OwnerGymRepository ownerGymRepository;

    public GymsDataManager() {
        FFApp.get().getAppComponent().inject(this);
    }

    public Single<List<Gym>> getAdminsGymsByEmail(String adminEmail) {
        return ownerAdminsRepository.getAdminsGymsEmailAsync(adminEmail)
                .flatMap(ownerGymRepository::getGymsByIdsAsync);
    }
}
