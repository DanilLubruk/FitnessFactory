package com.example.fitnessfactory.data.managers;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.AdminsRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class AdminsDataManager extends BaseManager {

    @Inject
    AdminsRepository adminsRepository;
    @Inject
    UserRepository userRepository;

    public AdminsDataManager() {
        FFApp.get().getAppComponent().inject(this);
    }

    public Single<List<AppUser>> getAdminsListAsync() {
        return adminsRepository.getAdminsEmailsAsync()
                .flatMap(userRepository::getUsersByEmailsAsync);
    }

    public Single<List<AppUser>> getAdminsListByGymIdAsync(String gymId) {
        return adminsRepository.getAdminsEmailsByGymIdAsync(gymId)
                .flatMap(userRepository::getUsersByEmailsAsync);
    }
}
