package com.example.fitnessfactory.mockHelpers.mockers;

import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.models.Admin;
import com.example.fitnessfactory.data.models.AdminAccessEntry;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.AdminsDataProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mockito.Mockito;

import io.reactivex.Single;

public class AdminsAccessManagerMocker {

    public static AdminsAccessManager createMock(AdminsAccessRepository accessRepository,
                                                    OwnerAdminsRepository ownersRepository) {
        AdminsAccessManager adminsAccessManager =
                new AdminsAccessManager(accessRepository, ownersRepository);

        Mockito.when(
                accessRepository
                        .isPersonnelWithThisEmailRegistered(Mockito.anyString(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String id = invocation.getArgument(0);
                    String email = invocation.getArgument(1);

                    for (AdminAccessEntry adminEntry : AdminsDataProvider.getAdminEntries()) {
                        if (id.equals(adminEntry.getOwnerId()) &&
                                email.equals(adminEntry.getUserEmail())) {
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

                    for (Admin admin : AdminsDataProvider.getAdmins()) {
                        if (email.equals(admin.getUserEmail())) {
                            return Single.just(true);
                        }
                    }

                    return Single.just(false);
                });

        Mockito.when(ownersRepository.getDeletePersonnelBatch(Mockito.any(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        Mockito.when(accessRepository.getDeletePersonnelAccessEntryBatch(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        return adminsAccessManager;
    }
}
