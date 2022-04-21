package com.example.fitnessfactory.data.models;

public class OrganisationData {

    public static final String NAME_FIELD = "name";
    public static final String ADDRESS_FIELD = "address";
    public static final String EMAIL_FIELD = "email";
    public static final String PHONE_FIELD = "phone";
    public static final String TAX_ID_FIELD = "taxId";
    public static final String BANK_DETAILS_FIELD = "bankDetails";

    private String name = "";
    private String address = "";
    private String email = "";
    private String phone = "";
    private String taxId = "";
    private String bankDetails = "";

    public OrganisationData() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(String bankDetails) {
        this.bankDetails = bankDetails;
    }

    public static boolean isNotNull(OrganisationData organisationData) {
        return organisationData != null && organisationData.getName() != null;
    }

    public boolean equals(OrganisationData organisationData) {
        return this.getName().equals(organisationData.getName()) &&
                this.getAddress().equals(organisationData.getAddress()) &&
                this.getEmail().equals(organisationData.getEmail()) &&
                this.getPhone().equals(organisationData.getPhone()) &&
                this.getTaxId().equals(organisationData.getTaxId()) &&
                this.getBankDetails().equals(organisationData.getBankDetails());
    }

    public void copy(OrganisationData organisationData) {
        this.setName(organisationData.getName());
        this.setAddress(organisationData.getAddress());
        this.setEmail(organisationData.getEmail());
        this.setPhone(organisationData.getPhone());
        this.setTaxId(organisationData.getTaxId());
        this.setBankDetails(organisationData.getBankDetails());
    }

    public void trimStrings() {
        setName(getName().trim());
        setAddress(getAddress().trim());
        setEmail(getEmail().trim());
        setPhone(getPhone().trim());
        setTaxId(getTaxId().trim());
        setBankDetails(getBankDetails().trim());
    }
}
