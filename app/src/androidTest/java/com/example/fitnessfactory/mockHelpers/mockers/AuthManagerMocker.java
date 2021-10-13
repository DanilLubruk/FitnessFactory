package com.example.fitnessfactory.mockHelpers.mockers;

import com.example.fitnessfactory.data.managers.AuthManager;
import com.example.fitnessfactory.data.repositories.OrganisationInfoRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.personnel.AdminsDataProvider;
import com.example.fitnessfactory.mockHelpers.mockers.access.AdminsAccessRepositoryMocker;
import com.example.fitnessfactory.system.FirebaseAuthManager;

public class AuthManagerMocker {

    private static AdminsDataProvider dataProvider = new AdminsDataProvider();

    public static AuthManager createMocker(UserRepository userRepository,
                                           AdminsAccessRepository adminsAccessRepository,
                                           FirebaseAuthManager firebaseAuthManager,
                                           OrganisationInfoRepository organisationInfoRepository) {
        return new AuthManager(
                        UserRepositoryMocker.createMocker(userRepository),
                        AdminsAccessRepositoryMocker.createMocker(adminsAccessRepository),
                        FirebaseAuthManagerMocker.createMocker(firebaseAuthManager),
                        OrganisationInfoRepositoryMocker.createMocker(organisationInfoRepository));
    }
}
