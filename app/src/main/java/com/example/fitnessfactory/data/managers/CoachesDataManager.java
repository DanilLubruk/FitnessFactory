package com.example.fitnessfactory.data.managers;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class CoachesDataManager extends BaseManager {

    @Inject
    OwnerCoachesRepository ownerCoachesRepository;
    @Inject
    UserRepository userRepository;

    public CoachesDataManager() {
        FFApp.get().getAppComponent().inject(this);
    }

    public Single<List<AppUser>> getCoachesListAsync() {
        return ownerCoachesRepository.getCoachesEmailsAsync()
                .flatMap(userRepository::getUsersByEmailsAsync);
    }
}
