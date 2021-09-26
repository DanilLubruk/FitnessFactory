package com.example.fitnessfactory.mockHelpers.mockers;

import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Coach;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.CoachesDataProvider;
import com.example.fitnessfactory.mockHelpers.mockdata.GymsDataProvider;
import com.example.fitnessfactory.mockHelpers.mockdata.UsersDataProvider;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class CoachesDataManagerMocker {

    public static CoachesDataManager createMock(OwnerCoachesRepository ownerRepository,
                                                UserRepository userRepository,
                                                OwnerGymRepository ownerGymRepository) {
        CoachesDataManager dataManager =
                new CoachesDataManager(ownerRepository, userRepository, ownerGymRepository);

        Mockito.when(ownerRepository.getPersonnelEmails())
                .thenAnswer(invocation -> {
                    List<String> emails = new ArrayList<>();

                    for (Coach coach : CoachesDataProvider.getCoaches()) {
                        emails.add(coach.getUserEmail());
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

                    for (Coach coach : CoachesDataProvider.getCoaches()) {
                        if (coach.getGymsIds() != null &&
                                coach.getGymsIds().contains(gymId)) {
                            emails.add(coach.getUserEmail());
                        }
                    }

                    return Single.just(emails);
                });

        Mockito.when(ownerRepository.getPersonnelGymsIdsByEmail(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String personnelEmail = invocation.getArgument(0);

                    for (Coach coach : CoachesDataProvider.getCoaches()) {
                        if (coach.getUserEmail().equals(personnelEmail)) {
                            return Single.just(coach.getGymsIds());
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
