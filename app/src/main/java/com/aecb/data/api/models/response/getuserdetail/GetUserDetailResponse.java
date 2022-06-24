package com.aecb.data.api.models.response.getuserdetail;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class GetUserDetailResponse {

    @SerializedName("firstname")
    private String firstName;

    @SerializedName("preferredLanguage")
    private String preferredLanguage;

    @SerializedName("gender")
    private int gender;

    @SerializedName("nationalityId")
    private String nationalityId;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("middlename")
    private String middleName;

    @SerializedName("lastname")
    private String lastName;

    @SerializedName("tcVersionNumber")
    private float tcVersionNumber;

    @SerializedName("nationality")
    private Object nationality;

    @SerializedName("passport")
    private String passport;

    @SerializedName("dob")
    private String dob;

    @SerializedName("name")
    private String name;

    @SerializedName("emiratesId")
    private String emiratesId;

    @SerializedName("email")
    private String email;

    @SerializedName("deviceId")
    private String deviceId;

    @SerializedName("preferredPaymentMethod")
    private String preferredPaymentMethod;

    public String getPreferredPaymentMethod() {
        return preferredPaymentMethod;
    }

    public void setPreferredPaymentMethod(String preferredPaymentMethod) {
        this.preferredPaymentMethod = preferredPaymentMethod;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @SerializedName("password")
    private String password;

    public void setFirstname(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstname() {
        return firstName;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getGender() {
        return gender;
    }

    public void setNationalityId(String nationalityId) {
        this.nationalityId = nationalityId;
    }

    public String getNationalityId() {
        return nationalityId;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMiddlename(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddlename() {
        return middleName;
    }

    public void setLastname(String lastName) {
        this.lastName = lastName;
    }

    public String getLastname() {
        return lastName;
    }

    public void setTcVersionNumber(float tcVersionNumber) {
        this.tcVersionNumber = tcVersionNumber;
    }

    public float getTcVersionNumber() {
        return tcVersionNumber;
    }

    public void setNationality(Object nationality) {
        this.nationality = nationality;
    }

    public Object getNationality() {
        return nationality;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getPassport() {
        return passport;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDob() {
        return dob;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmiratesId(String emiratesId) {
        this.emiratesId = emiratesId;
    }

    public String getEmiratesId() {
        return emiratesId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "firstname = '" + firstName + '\'' +
                        ",preferredLanguage = '" + preferredLanguage + '\'' +
                        ",gender = '" + gender + '\'' +
                        ",nationalityId = '" + nationalityId + '\'' +
                        ",mobile = '" + mobile + '\'' +
                        ",middlename = '" + middleName + '\'' +
                        ",lastname = '" + lastName + '\'' +
                        ",tcVersionNumber = '" + tcVersionNumber + '\'' +
                        ",nationality = '" + nationality + '\'' +
                        ",passport = '" + passport + '\'' +
                        ",dob = '" + dob + '\'' +
                        ",name = '" + name + '\'' +
                        ",emiratesId = '" + emiratesId + '\'' +
                        ",email = '" + email + '\'' +
                        ",deviceId ='" + deviceId + '\'' +
                        "}";
    }
}