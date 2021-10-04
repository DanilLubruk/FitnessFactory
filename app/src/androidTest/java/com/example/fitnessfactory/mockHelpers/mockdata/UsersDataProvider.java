package com.example.fitnessfactory.mockHelpers.mockdata;

import com.example.fitnessfactory.data.models.AppUser;

import java.util.ArrayList;
import java.util.List;

public class UsersDataProvider {

    private List<AppUser> users;

    public UsersDataProvider() {
        initData();
    }

    public List<AppUser> getUsers() {
        return users;
    }

    public void addUser(AppUser appUser) {
        users.add(appUser);
    }

    private void initData() {
        users = new ArrayList<AppUser>() {{
            add(AppUser
                    .builder()
                    .setId("userId1")
                    .setName("User1")
                    .setEmail("useremail1")
                    .build());

            add(AppUser
                    .builder()
                    .setId("userId2")
                    .setName("User2")
                    .setEmail("useremail2")
                    .build());

            add(AppUser
                    .builder()
                    .setId("userId3")
                    .setName("User3")
                    .setEmail("useremail3")
                    .build());

            add(AppUser
                    .builder()
                    .setId("userId4")
                    .setName("User4")
                    .setEmail("useremail4")
                    .build());

            add(AppUser
                    .builder()
                    .setId("userId5")
                    .setName("User5")
                    .setEmail("useremail5")
                    .build());
        }};
    }
}
