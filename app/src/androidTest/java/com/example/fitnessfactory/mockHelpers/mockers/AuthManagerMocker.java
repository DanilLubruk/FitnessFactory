package com.example.fitnessfactory.mockHelpers.mockers;

import com.example.fitnessfactory.data.managers.AuthManager;
import com.example.fitnessfactory.data.models.AdminAccessEntry;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.OrganisationInfoRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.personnel.AdminsDataProvider;
import com.example.fitnessfactory.system.FirebaseAuthManager;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class AuthManagerMocker {

    private static AdminsDataProvider dataProvider = new AdminsDataProvider();

    public static AuthManager createMocker(UserRepository userRepository,
                                           AdminsAccessRepository adminsAccessRepository,
                                           FirebaseAuthManager firebaseAuthManager,
                                           OrganisationInfoRepository organisationInfoRepository) {
        AuthManager authManager =
                new AuthManager(
                        UserRepositoryMocker.createMocker(userRepository),
                        adminsAccessRepository,
                        FirebaseAuthManagerMocker.createMocker(firebaseAuthManager),
                        OrganisationInfoRepositoryMocker.createMocker(organisationInfoRepository));

        Mockito.when(adminsAccessRepository.getOwnersByInvitedEmail(Mockito.any()))
                .thenAnswer(invocation -> {
                    AppUser appUser = invocation.getArgument(0);
                    List<String> ownerIds = new ArrayList<>();
                    ownerIds.add(appUser.getId());

                    for (AdminAccessEntry adminEntry : dataProvider.getAccessEntries()) {
                        if (adminEntry.getUserEmail().equals(appUser.getEmail())) {
                            ownerIds.add(adminEntry.getOwnerId());
                        }
                    }

                    return Single.just(ownerIds);
                });

        return authManager;
    }
}
