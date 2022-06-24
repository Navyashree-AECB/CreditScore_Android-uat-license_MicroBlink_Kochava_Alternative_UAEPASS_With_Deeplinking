package com.aecb.data.api.models.buycreditreport;

import com.google.gson.annotations.SerializedName;

public class BuyCreditReportRequest {

    @SerializedName("lastName")
    private String lastName = "";

    @SerializedName("gender")
    private String gender;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("channel")
    private int channel;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("isAuthenticated")
    private boolean isAuthenticated;

    @SerializedName("reportType")
    private String reportType;

    @SerializedName("firstName")
    private String firstName = "";

    @SerializedName("tcVersionNumber")
    private float tcVersionNumber;

    @SerializedName("nationality")
    private String nationality;

    @SerializedName("passport")
    private String passport;

    @SerializedName("smartpassData")
    private String smartpassData = "";

    @SerializedName("dob")
    private String dob;

    @SerializedName("authenticatedId")
    private String authenticatedId = "";

    @SerializedName("preferredlanguage")
    private String preferredlanguage;

    @SerializedName("middleName")
    private String middleName = "";

    @SerializedName("emiratesID")
    private String emiratesID;

    @SerializedName("email")
    private String email;

    @SerializedName("paymentSource")
    private String paymentSource;

    public String getPaymentSource() {
        return paymentSource;
    }

    public void setPaymentSource(String paymentSource) {
        this.paymentSource = paymentSource;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isIsAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public float getTcVersionNumber() {
        return tcVersionNumber;
    }

    public void setTcVersionNumber(float tcVersionNumber) {
        this.tcVersionNumber = tcVersionNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getSmartpassData() {
        return smartpassData;
    }

    public void setSmartpassData(String smartpassData) {
        this.smartpassData = smartpassData;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAuthenticatedId() {
        return authenticatedId;
    }

    public void setAuthenticatedId(String authenticatedId) {
        this.authenticatedId = authenticatedId;
    }

    public String getPreferredlanguage() {
        return preferredlanguage;
    }

    public void setPreferredlanguage(String preferredlanguage) {
        this.preferredlanguage = preferredlanguage;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmiratesID() {
        return emiratesID;
    }

    public void setEmiratesID(String emiratesID) {
        this.emiratesID = emiratesID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}