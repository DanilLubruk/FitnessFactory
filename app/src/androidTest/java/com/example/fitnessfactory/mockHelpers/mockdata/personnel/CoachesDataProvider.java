package com.example.fitnessfactory.mockHelpers.mockdata.personnel;

import com.example.fitnessfactory.data.models.Coach;
import com.example.fitnessfactory.data.models.CoachAccessEntry;
import java.util.List;

public class CoachesDataProvider extends PersonnelDataProvider<Coach, CoachAccessEntry> {

    public CoachesDataProvider() {
        super();
    }

    @Override
    protected Coach createPersonnel(String userEmail, List<String> gymsIds) {
        return Coach
                .builder()
                .setUserEmail(userEmail)
                .setGymsIds(gymsIds)
                .build();
    }

    @Override
    protected CoachAccessEntry createPersonnelAccessEntry(String userEmail, String ownerId) {
        return CoachAccessEntry
                .builder()
                .setUserEmail(userEmail)
                .setOwnerId(ownerId)
                .build();
    }
}
