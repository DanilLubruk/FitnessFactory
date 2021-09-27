package com.example.fitnessfactory.mockHelpers.mockdata;

import com.example.fitnessfactory.data.models.Admin;
import com.example.fitnessfactory.data.models.AdminAccessEntry;
import java.util.List;

public class AdminsDataProvider extends PersonnelDataProvider<Admin, AdminAccessEntry> {

    public AdminsDataProvider() {
        super();
    }

    @Override
    protected Admin createPersonnel(String userEmail, List<String> gymsIds) {
        return Admin
                .builder()
                .setUserEmail(userEmail)
                .setGymsIds(gymsIds)
                .build();
    }

    @Override
    protected AdminAccessEntry createPersonnelAccessEntry(String userEmail, String ownerId) {
        return AdminAccessEntry
                .builder()
                .setUserEmail(userEmail)
                .setOwnerId(ownerId)
                .build();
    }
}
