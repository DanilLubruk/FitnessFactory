package com.example.fitnessfactory.mockHelpers.mockers;

import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.data.models.Admin;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.AdminsDataProvider;
import com.example.fitnessfactory.mockHelpers.mockdata.GymsDataProvider;
import com.example.fitnessfactory.mockHelpers.mockdata.UsersDataProvider;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class AdminsDataManagerMocker {

    public static AdminsDataManager createMock(OwnerAdminsRepository ownerRepository,
                                               UserRepository userRepository,
                                               OwnerGymRepository ownerGymRepository) {
        AdminsDataManager dataManager =
                new AdminsDataManager(ownerRepository, userRepository, ownerGymRepository);

        Mockito.when(ownerRepository.getPersonnelEmails())
                .thenAnswer(invocation -> {
                    List<String> emails = new ArrayList<>();

                    for (Admin admin : AdminsDataProvider.getAdmins()) {
                        emails.add(admin.getUserEmail());
                    }

                    return Single.just(emails);
                });

        Mockito.when(userRepository.getUsersByEmailsAsync(Mockito.anyList()))
                .thenAnswer(invocation -> {
                    List<String> emails = invocation.getArgument(0);
                    List<AppUser> users = new ArrayList<>();

                    for (AppUser appUser : UsersDataProvider.getUsers()) {
                        if (emails.contains(appUser.getEmail())) {
                            users.add(appUser);
                        }
                    }

                    return Single.just(users);
                });

        Mockito.when(ownerRepository.getPersonnelEmailsByGymId(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String gymId = invocation.getArgument(0);
                    List<String> emails = new ArrayList<>();

                    for (Admin admin : AdminsDataProvider.getAdmins()) {
                        if (admin.getGymsIds() != null &&
                                admin.getGymsIds().contains(gymId)) {
                            emails.add(admin.getUserEmail());
                        }
                    }

                    return Single.just(emails);
                });

        Mockito.when(ownerRepository.getPersonnelGymsIdsByEmail(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String personnelEmail = invocation.getArgument(0);

                    for (Admin admin : AdminsDataProvider.getAdmins()) {
                        if (admin.getUserEmail().equals(personnelEmail)) {
                            return Single.just(admin.getGymsIds());
                        }
                    }

                    return Single.just(new ArrayList<String>());
                });

        Mockito.when(ownerGymRepository.getGymsByIdsAsync(Mockito.anyList()))
                .thenAnswer(invocation -> {
                    List<String> gymsIds = invocation.getArgument(0);
                    List<Gym> gyms = new ArrayList<>();

                    for (Gym gym : GymsDataProvider.getGyms()) {
                        if (gymsIds.contains(gym.getId())) {
                            gyms.add(gym);
                        }
                    }

                    return Single.just(gyms);
                });

        return dataManager;
    }
}
