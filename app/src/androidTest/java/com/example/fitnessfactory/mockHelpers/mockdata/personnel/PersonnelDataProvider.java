package com.example.fitnessfactory.mockHelpers.mockdata.personnel;

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
            add(createPersonnelAccessEntry("useremail1", "userId2"));
            add(createPersonnelAccessEntry("useremail2", "userId2"));
            add(createPersonnelAccessEntry("useremail3", "userId2"));
            add(createPersonnelAccessEntry("useremail4", "userId2"));
            add(createPersonnelAccessEntry("useremail1", "userId3"));
            add(createPersonnelAccessEntry("useremail5", "userId3"));
            add(createPersonnelAccessEntry("useremail6", "userId3"));
        }};

        personnel = new ArrayList<P>() {{
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
