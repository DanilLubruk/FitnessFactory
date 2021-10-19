package com.example.fitnessfactory.mockHelpers.mockers.data;

import com.example.fitnessfactory.data.models.Personnel;
import com.example.fitnessfactory.data.models.PersonnelAccessEntry;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.personnel.PersonnelDataProvider;
import com.example.fitnessfactory.mockHelpers.mockers.UserRepositoryMocker;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

abstract class PersonnelDataManagerMocker {

    static <P extends Personnel,
            PE extends PersonnelAccessEntry>
    void setupMock(PersonnelDataProvider<P, PE> dataProvider,
                   OwnerPersonnelRepository ownerRepository,
                   UserRepository userRepository) {
        Mockito.when(ownerRepository.getPersonnelEmailsAsync())
                .thenAnswer(invocation -> {
                    List<String> emails = new ArrayList<>();

                    for (P personnel : dataProvider.getPersonnel()) {
                        emails.add(personnel.getUserEmail());
                    }

                    return Single.just(emails);
                });

        userRepository = UserRepositoryMocker.createMocker(userRepository);

        Mockito.when(ownerRepository.getPersonnelEmailsByGymId(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String gymId = invocation.getArgument(0);
                    List<String> emails = new ArrayList<>();

                    for (P personnel : dataProvider.getPersonnel()) {
                        if (personnel.getGymsIds() != null &&
                                personnel.getGymsIds().contains(gymId)) {
                            emails.add(personnel.getUserEmail());
                        }
                    }

                    return Single.just(emails);
                });

        Mockito.when(ownerRepository.getPersonnelGymsIdsByEmail(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String personnelEmail = invocation.getArgument(0);

                    for (P personnel : dataProvider.getPersonnel()) {
                        if (personnel.getUserEmail().equals(personnelEmail)) {
                            return Single.just(personnel.getGymsIds());
                        }
                    }

                    return Single.just(new ArrayList<String>());
                });
    }
}
