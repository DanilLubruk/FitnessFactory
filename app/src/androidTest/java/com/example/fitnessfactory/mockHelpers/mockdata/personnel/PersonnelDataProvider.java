package com.example.fitnessfactory.mockHelpers.mockdata.personnel;

import com.example.fitnessfactory.data.models.Personnel;
import com.example.fitnessfactory.data.models.PersonnelAccessEntry;

import java.util.ArrayList;
import java.util.List;

public class PersonnelDataProvider {

    private List<PersonnelAccessEntry> accessEntries;
    private List<Personnel> personnel;

    public PersonnelDataProvider() {
        initData();
    }

    public List<PersonnelAccessEntry> getAccessEntries() {
        return accessEntries;
    }

    public List<Personnel> getPersonnel() {
        return personnel;
    }

    private Personnel createPersonnel(String userEmail, List<String> gymsIds) {
        return Personnel
                .builder()
                .setUserEmail(userEmail)
                .setGymsIds(gymsIds)
                .build();
    }

    private PersonnelAccessEntry createPersonnelAccessEntry(String userEmail, String ownerId) {
        return PersonnelAccessEntry
                .builder()
                .setUserEmail(userEmail)
                .setOwnerId(ownerId)
                .build();
    }

    private void initData() {
        accessEntries = new ArrayList<PersonnelAccessEntry>() {{
            add(createPersonnelAccessEntry("useremail1", "userId2"));
            add(createPersonnelAccessEntry("useremail2", "userId2"));
            add(createPersonnelAccessEntry("useremail3", "userId2"));
            add(createPersonnelAccessEntry("useremail4", "userId2"));
            add(createPersonnelAccessEntry("useremail1", "userId3"));
            add(createPersonnelAccessEntry("useremail5", "userId3"));
            add(createPersonnelAccessEntry("useremail6", "userId3"));
        }};

        personnel = new ArrayList<Personnel>() {{
            add(createPersonnel(
                    "useremail1",
                    new ArrayList<String>(){{
                add("gymId1");
            }}));

            add(createPersonnel(
                    "useremail2",
                    new ArrayList<String>(){{
                        add("gymId1");
                    }}));

            add(createPersonnel(
                    "useremail3",
                    new ArrayList<String>(){{
                        add("gymId2");
                    }}));

            add(createPersonnel(
                    "useremail4",
                    new ArrayList<String>(){{
                        add("gymId2");
                        add("gymId3");
                    }}));
        }};
    }
}
