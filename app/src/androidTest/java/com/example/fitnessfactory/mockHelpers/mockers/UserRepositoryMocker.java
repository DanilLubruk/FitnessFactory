package com.example.fitnessfactory.mockHelpers.mockers;

import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.UsersDataProvider;
import com.example.fitnessfactory.utils.UsersUtils;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class UserRepositoryMocker {

    public static UserRepository createMocker(UserRepository userRepository) {
        UsersDataProvider dataProvider = new UsersDataProvider();

        Mockito.when(userRepository.getOwnersByIds(Mockito.anyList(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    List<String> ownerIds = invocation.getArgument(0);
                    String currentUserId = invocation.getArgument(1);
                    List<AppUser> owners = new ArrayList<>();

                    for (AppUser appUser : dataProvider.getUsers()) {
                        if (ownerIds.contains(appUser.getId())) {
                            owners.add(appUser);
                        }
                    }

                    owners = UsersUtils.makeCurrentUserFirstInList(owners, currentUserId);

                    return Single.just(owners);
                });

        Mockito.when(userRepository.registerUser(Mockito.any(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String userEmail = invocation.getArgument(0);
                    String userName = invocation.getArgument(1);

                    AppUser appUser = AppUser
                            .builder()
                            .setId("newUserId")
                            .setEmail(userEmail)
                            .setName(userName)
                            .build();
                    dataProvider.addUser(appUser);

                    return Single.just(appUser);
                });

        Mockito.when(userRepository.getAppUserByEmailAsync(Mockito.any()))
                .thenAnswer(invocation -> {
                    String userEmail = invocation.getArgument(0);
                    List<AppUser> appUsers = new ArrayList<>();

                    for (AppUser appUser : dataProvider.getUsers()) {
                        if (appUser.getEmail().equals(userEmail)) {
                            appUsers.add(appUser);
                        }
                    }

                    if (appUsers.size() == 0) {
                        return Single.error(new Exception("Users data is empty"));
                    }

                    if (appUsers.size() > 1) {
                        return Single.error(new Exception("User email not unique"));
                    }

                    return Single.just(appUsers.get(0));
                });

        Mockito.when(userRepository.getUsersByIdsAsync(Mockito.anyList()))
                .thenAnswer(invocation -> {
                    List<String> emails = invocation.getArgument(0);
                    List<AppUser> users = new ArrayList<>();

                    for (AppUser appUser : dataProvider.getUsers()) {
                        if (emails.contains(appUser.getEmail())) {
                            users.add(appUser);
                        }
                    }

                    return Single.just(users);
                });

        Mockito.when(userRepository.isUserRegisteredAsync(Mockito.any()))
                .thenAnswer(invocation -> {
                    String userEmail = invocation.getArgument(0);

                    for (AppUser appUser : dataProvider.getUsers()) {
                        if (appUser.getEmail().equals(userEmail)) {
                            return Single.just(true);
                        }
                    }

                    return Single.just(false);
                });

        return userRepository;
    }
}
