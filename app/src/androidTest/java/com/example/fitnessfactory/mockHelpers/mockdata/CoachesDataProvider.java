package com.example.fitnessfactory.mockHelpers.mockdata;

import com.example.fitnessfactory.data.models.Coach;
import com.example.fitnessfactory.data.models.CoachAccessEntry;

import java.util.ArrayList;
import java.util.List;

public class CoachesDataProvider {

    private static List<CoachAccessEntry> coachesEntries;
    private static List<Coach> coaches;

    public static List<CoachAccessEntry> getCoachesEntries() {
        return coachesEntries;
    }

    public static List<Coach> getCoaches() {
        return coaches;
    }

    static {
        coachesEntries = new ArrayList<CoachAccessEntry>() {{
            add(CoachAccessEntry
                    .builder()
                    .setUserEmail("userEmail1")
                    .setOwnerId("ownerId1")
                    .build());

            add(CoachAccessEntry
                    .builder()
                    .setUserEmail("userEmail2")
                    .setOwnerId("ownerId1")
                    .build());

            add(CoachAccessEntry
                    .builder()
                    .setUserEmail("userEmail3")
                    .setOwnerId("ownerId1")
                    .build());

            add(CoachAccessEntry
                    .builder()
                    .setUserEmail("userEmail4")
                    .setOwnerId("ownerId1")
                    .build());

            add(CoachAccessEntry
                    .builder()
                    .setUserEmail("userEmail1")
                    .setOwnerId("ownerId2")
                    .build());

            add(CoachAccessEntry
                    .builder()
                    .setUserEmail("userEmail5")
                    .setOwnerId("ownerId2")
                    .build());
        }};

        coaches = new ArrayList<Coach>() {{
            add(Coach
                    .builder()
                    .setUserEmail("userEmail1")
                    .setGymsIds(new ArrayList<String>(){{
                        add("gymId1");
                    }})
                    .build());

            add(Coach
                    .builder()
                    .setUserEmail("userEmail2")
                    .setGymsIds(new ArrayList<String>(){{
                        add("gymId1");
                    }})
                    .build());

            add(Coach
                    .builder()
                    .setUserEmail("userEmail3")
                    .setGymsIds(new ArrayList<String>(){{
                        add("gymId2");
                    }})
                    .build());

            add(Coach
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
