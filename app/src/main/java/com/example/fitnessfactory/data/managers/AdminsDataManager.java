package com.example.fitnessfactory.data.managers;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class AdminsDataManager extends BaseManager {

    @Inject
    OwnerAdminsRepository ownerAdminsRepository;
    @Inject
    UserRepository userRepository;

    public AdminsDataManager() {
        FFApp.get().getAppComponent().inject(this);
    }

    public Single<List<AppUser>> getAdminsListAsync() {
        return ownerAdminsRepository.getAdminsEmailsAsync()
                .flatMap(userRepository::getUsersByEmailsAsync);
    }

    public Single<List<AppUser>> getAdminsListByGymIdAsync(String gymId) {
        return ownerAdminsRepository.getAdminsEmailsByGymIdAsync(gymId)
                .flatMap(userRepository::getUsersByEmailsAsync);
    }
}
