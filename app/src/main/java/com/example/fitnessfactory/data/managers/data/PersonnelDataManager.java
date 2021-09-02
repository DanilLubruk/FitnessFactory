package com.example.fitnessfactory.data.managers.data;

import com.example.fitnessfactory.data.managers.BaseManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;

import java.util.List;

import io.reactivex.Single;

public abstract class PersonnelDataManager extends BaseManager {

    protected abstract OwnerPersonnelRepository getOwnerRepository();

    protected abstract UserRepository getUserRepository();

    public Single<List<AppUser>> getPersonnelListAsync() {
        return getOwnerRepository().getPersonnelEmails()
                .flatMap(getUserRepository()::getUsersByEmailsAsync);
    }

    public Single<List<AppUser>> getPersonnelListByGymIdAsync(String gymId) {
        return getOwnerRepository().getPersonnelEmailsByGymId(gymId)
                .flatMap(getUserRepository()::getUsersByEmailsAsync);
    }
}
