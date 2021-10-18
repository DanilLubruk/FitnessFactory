package com.example.fitnessfactory.data.models;

public class OrganisationData {

    public static final String NAME_FIELD = "name";

    private String name;

    public OrganisationData() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static boolean isNotNull(OrganisationData organisationData) {
        return organisationData != null && organisationData.getName() != null;
    }

    public boolean equals(OrganisationData organisationData) {
        return this.getName().equals(organisationData.getName());
    }

    public void copy(OrganisationData organisationData) {
        this.setName(organisationData.getName());
    }
}
