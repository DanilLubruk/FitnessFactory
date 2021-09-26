package com.example.fitnessfactory.mockHelpers.mockers;

import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.models.Coach;
import com.example.fitnessfactory.data.models.CoachAccessEntry;
import com.example.fitnessfactory.data.repositories.access.CoachesAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.CoachesDataProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mockito.Mockito;
import io.reactivex.Single;

public class CoachesAccessManagerMocker {

    public static CoachesAccessManager createMock(CoachesAccessRepository accessRepository,
                                                    OwnerCoachesRepository ownersRepository) {
        CoachesAccessManager coachesAccessManager =
                new CoachesAccessManager(accessRepository, ownersRepository);

        Mockito.when(
                accessRepository
                        .isPersonnelWithThisEmailRegistered(Mockito.anyString(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String id = invocation.getArgument(0);
                    String email = invocation.getArgument(1);

                    for (CoachAccessEntry coachEntry : CoachesDataProvider.getCoachesEntries()) {
                        if (id.equals(coachEntry.getOwnerId()) &&
                                email.equals(coachEntry.getUserEmail())) {
                            return Single.just(true);
                        }
                    }

                    return Single.just(false);
                });

        Mockito.when(accessRepository
                .getRegisterPersonnelAccessEntryBatch(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        Mockito.when(ownersRepository
                .getAddPersonnelBatch(Mockito.any(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        Mockito.when(ownersRepository.isPersonnelWithThisEmailAdded(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String email = invocation.getArgument(0);

                    for (Coach coach : CoachesDataProvider.getCoaches()) {
                        if (email.equals(coach.getUserEmail())) {
                            return Single.just(true);
                        }
                    }

                    return Single.just(false);
                });

        Mockito.when(ownersRepository.getDeletePersonnelBatch(Mockito.any(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        Mockito.when(accessRepository.getDeletePersonnelAccessEntryBatch(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        return coachesAccessManager;
    }
}
