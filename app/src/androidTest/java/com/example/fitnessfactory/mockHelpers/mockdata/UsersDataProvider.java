package com.example.fitnessfactory.mockHelpers.mockdata;

import com.example.fitnessfactory.data.models.AppUser;

import java.util.ArrayList;
import java.util.List;

public class UsersDataProvider {

    private static List<AppUser> users;

    public static List<AppUser> getUsers() {
        return users;
    }

    static {
        users = new ArrayList<AppUser>() {{
           add(AppUser
                   .builder()
                   .setId("userId1")
                   .setName("User1")
                   .setEmail("userEmail1")
                   .build());

            add(AppUser
                    .builder()
                    .setId("userId2")
                    .setName("User2")
                    .setEmail("userEmail2")
                    .build());

            add(AppUser
                    .builder()
                    .setId("userId3")
                    .setName("User3")
                    .setEmail("userEmail3")
                    .build());

            add(AppUser
                    .builder()
                    .setId("userId4")
                    .setName("User4")
                    .setEmail("userEmail4")
                    .build());

            add(AppUser
                    .builder()
                    .setId("userId5")
                    .setName("User5")
                    .setEmail("userEmail5")
                    .build());
        }};
    }
}
