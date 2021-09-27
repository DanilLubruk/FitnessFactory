package com.example.fitnessfactory.mockHelpers.mockdata;

import com.example.fitnessfactory.data.models.Personnel;
import com.example.fitnessfactory.data.models.PersonnelAccessEntry;

import java.util.ArrayList;
import java.util.List;

public abstract class PersonnelDataProvider<P extends Personnel, PE extends PersonnelAccessEntry> {

    private List<PE> accessEntries;
    private List<P> personnel;

    public PersonnelDataProvider() {
        initData();
    }

    public List<PE> getAccessEntries() {
        return accessEntries;
    }

    public List<P> getPersonnel() {
        return personnel;
    }

    protected abstract P createPersonnel(String userEmail, List<String> gymsIds);

    protected abstract PE createPersonnelAccessEntry(String userEmail, String ownerId);

    private void initData() {
        accessEntries = new ArrayList<PE>() {{
            add(createPersonnelAccessEntry("userEmail1", "ownerId1"));
            add(createPersonnelAccessEntry("userEmail2", "ownerId1"));
            add(createPersonnelAccessEntry("userEmail3", "ownerId1"));
            add(createPersonnelAccessEntry("userEmail4", "ownerId1"));
            add(createPersonnelAccessEntry("userEmail1", "ownerId2"));
            add(createPersonnelAccessEntry("userEmail5", "ownerId2"));

        }};

        personnel = new ArrayList<P>() {{
            add(createPersonnel(
                    "userEmail1",
                    new ArrayList<String>(){{
                add("gymId1");
            }}));

            add(createPersonnel(
                    "userEmail2",
                    new ArrayList<String>(){{
                        add("gymId1");
                    }}));

            add(createPersonnel(
                    "userEmail3",
                    new ArrayList<String>(){{
                        add("gymId2");
                    }}));

            add(createPersonnel(
                    "userEmail4",
                    new ArrayList<String>(){{
                        add("gymId2");
                        add("gymId3");
                    }}));
        }};
    }
}
