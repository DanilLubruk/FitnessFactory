package com.example.fitnessfactory.mockHelpers.mockdata;

import com.example.fitnessfactory.data.models.Admin;
import com.example.fitnessfactory.data.models.AdminAccessEntry;

import java.util.ArrayList;
import java.util.List;

public class AdminsDataProvider {

    private static List<AdminAccessEntry> adminEntries;
    private static List<Admin> admins;

    public static List<AdminAccessEntry> getAdminEntries() {
        return adminEntries;
    }

    public static List<Admin> getAdmins() {
        return admins;
    }

    static {
        adminEntries = new ArrayList<AdminAccessEntry>() {{
            add(AdminAccessEntry
                    .builder()
                    .setUserEmail("userEmail1")
                    .setOwnerId("ownerId1")
                    .build());

            add(AdminAccessEntry
                    .builder()
                    .setUserEmail("userEmail2")
                    .setOwnerId("ownerId1")
                    .build());

            add(AdminAccessEntry
                    .builder()
                    .setUserEmail("userEmail3")
                    .setOwnerId("ownerId1")
                    .build());

            add(AdminAccessEntry
                    .builder()
                    .setUserEmail("userEmail4")
                    .setOwnerId("ownerId1")
                    .build());

            add(AdminAccessEntry
                    .builder()
                    .setUserEmail("userEmail1")
                    .setOwnerId("ownerId2")
                    .build());

            add(AdminAccessEntry
                    .builder()
                    .setUserEmail("userEmail5")
                    .setOwnerId("ownerId2")
                    .build());
        }};

        admins = new ArrayList<Admin>() {{
            add(Admin
                    .builder()
                    .setUserEmail("userEmail1")
                    .setGymsIds(new ArrayList<String>(){{
                        add("gymId1");
                    }})
                    .build());

            add(Admin
                    .builder()
                    .setUserEmail("userEmail2")
                    .setGymsIds(new ArrayList<String>(){{
                        add("gymId1");
                    }})
                    .build());

            add(Admin
                    .builder()
                    .setUserEmail("userEmail3")
                    .setGymsIds(new ArrayList<String>(){{
                        add("gymId2");
                    }})
                    .build());

            add(Admin
                    .builder()
                    .setUserEmail("userEmail4")
                    .setGymsIds(new ArrayList<String>(){{
                        add("gymId2");
                        add("gymId3");
                    }})
                    .build());
        }};
    }
}
