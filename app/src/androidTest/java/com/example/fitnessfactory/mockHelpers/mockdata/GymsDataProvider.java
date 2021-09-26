package com.example.fitnessfactory.mockHelpers.mockdata;

import com.example.fitnessfactory.data.models.Gym;

import java.util.ArrayList;
import java.util.List;

public class GymsDataProvider {

    private static List<Gym> gyms;

    public static List<Gym> getGyms() {
        return gyms;
    }

    static {
        gyms = new ArrayList<Gym>() {{
            add(Gym
                    .builder()
                    .setId("gymId1")
                    .setName("gymName1")
                    .setAddress("gymAddress1")
                    .build());

            add(Gym
                    .builder()
                    .setId("gymId2")
                    .setName("gymName2")
                    .setAddress("gymAddress2")
                    .build());

            add(Gym
                    .builder()
                    .setId("gymId3")
                    .setName("gymName3")
                    .setAddress("gymAddress3")
                    .build());

            add(Gym
                    .builder()
                    .setId("gymId4")
                    .setName("gymName4")
                    .setAddress("gymAddress4")
                    .build());
        }};
    }
}
