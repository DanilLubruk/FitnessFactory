package com.example.fitnessfactory.utils;

import com.example.fitnessfactory.data.models.AppUser;

import java.util.List;

public class UsersUtils {

    public static List<AppUser> makeCurrentUserFirstInList(List<AppUser> owners, String currentUserId) {
        for (int i = 0; i < owners.size(); i++) {
            AppUser currentUser = owners.get(i);
            if (currentUser.getId().equals(currentUserId)) {
                AppUser currentFirstUser = owners.get(0);
                owners.set(0, currentUser);
                owners.set(i, currentFirstUser);
                break;
            }
        }

        return owners;
    }
}
