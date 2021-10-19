package com.example.fitnessfactory.mockHelpers.mockers.access;

import com.example.fitnessfactory.data.models.Personnel;
import com.example.fitnessfactory.data.models.PersonnelAccessEntry;
import com.example.fitnessfactory.data.repositories.access.PersonnelAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.personnel.PersonnelDataProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mockito.Mockito;

import io.reactivex.Single;

abstract class PersonnelAccessManagerMocker {

    static <P extends Personnel,
            PE extends PersonnelAccessEntry>
    void setupMock(PersonnelDataProvider<P, PE> dataProvider,
                   PersonnelAccessRepository accessRepository,
                   OwnerPersonnelRepository ownersRepository) {
        Mockito.when(
                accessRepository
                        .isPersonnelWithThisEmailRegisteredAsync(Mockito.anyString(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String id = invocation.getArgument(0);
                    String email = invocation.getArgument(1);

                    for (PE accessEntry : dataProvider.getAccessEntries()) {
                        if (id.equals(accessEntry.getOwnerId()) &&
                                email.equals(accessEntry.getUserEmail())) {
                            return Single.just(true);
                        }
                    }

                    return Single.just(false);
                });

        Mockito.when(accessRepository
                .getRegisterPersonnelAccessEntryBatchAsync(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        Mockito.when(ownersRepository
                .getAddPersonnelBatchAsync(Mockito.any(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        Mockito.when(ownersRepository.isPersonnelWithThisEmailAddedAsync(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String email = invocation.getArgument(0);

                    for (P personnel : dataProvider.getPersonnel()) {
                        if (email.equals(personnel.getUserEmail())) {
                            return Single.just(true);
                        }
                    }

                    return Single.just(false);
                });

        Mockito.when(ownersRepository.getDeletePersonnelBatchAsync(Mockito.any(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        Mockito.when(accessRepository.getDeletePersonnelAccessEntryBatchAsync(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));
    }
}
