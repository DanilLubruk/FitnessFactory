package com.example.fitnessfactory.mockHelpers.mockers.access;

import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.PersonnelAccessEntry;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.personnel.PersonnelDataProvider;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class AdminsAccessRepositoryMocker {

    private static PersonnelDataProvider dataProvider = new PersonnelDataProvider();

    public static AdminsAccessRepository createMocker(AdminsAccessRepository adminsAccessRepository) {
        Mockito.when(adminsAccessRepository.getOwnersByInvitedEmail(Mockito.any()))
                .thenAnswer(invocation -> {
                    AppUser appUser = invocation.getArgument(0);
                    List<String> ownerIds = new ArrayList<>();
                    ownerIds.add(appUser.getId());

                    for (PersonnelAccessEntry adminEntry : dataProvider.getAccessEntries()) {
                        if (adminEntry.getUserId().equals(appUser.getEmail())) {
                            ownerIds.add(adminEntry.getOwnerId());
                        }
                    }

                    return Single.just(ownerIds);
                });

        return adminsAccessRepository;
    }
}
