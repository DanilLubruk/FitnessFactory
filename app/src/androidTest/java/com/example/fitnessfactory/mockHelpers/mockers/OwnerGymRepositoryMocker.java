package com.example.fitnessfactory.mockHelpers.mockers;

import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.GymsDataProvider;

import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class OwnerGymRepositoryMocker {

    public static OwnerGymRepository createMocker(OwnerGymRepository ownerGymRepository) {
        Mockito.when(ownerGymRepository.getGymAsync(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String gymId = invocation.getArgument(0);
                    List<Gym> gyms = new ArrayList<>();

                    for (Gym gym : GymsDataProvider.getGyms()) {
                        if (gym.getId().equals(gymId)) {
                            gyms.add(gym);
                        }
                    }

                    if (gyms.size() == 0) {
                        return Single.error(new Exception("Empty gym data. Check the mocker"));
                    }

                    if (gyms.size() > 1) {
                        return Single.error(new Exception("Not unique gym data. Check the mocker"));
                    }

                    return Single.just(gyms.get(0));
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

        Mockito.when(ownerGymRepository.saveAsync(Mockito.any()))
                .thenAnswer(invocation -> {
                   Gym gym = invocation.getArgument(0);

                   return Single.just(gym.getId());
                });

        return ownerGymRepository;
    }
}
