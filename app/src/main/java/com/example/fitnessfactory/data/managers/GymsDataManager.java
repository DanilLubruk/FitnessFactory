package com.example.fitnessfactory.data.managers;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.AdminsRepository;
import com.example.fitnessfactory.data.repositories.GymRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class GymsDataManager extends BaseManager {

    @Inject
    AdminsRepository adminsRepository;
    @Inject
    GymRepository gymRepository;

    public GymsDataManager() {
        FFApp.get().getAppComponent().inject(this);
    }

    public Single<List<Gym>> getGymsByIdsAsync(String adminEmail) {
        return adminsRepository.getAdminsGymsIdsAsync(adminEmail)
                .flatMap(gymRepository::getGymsByIds);
    }
}
