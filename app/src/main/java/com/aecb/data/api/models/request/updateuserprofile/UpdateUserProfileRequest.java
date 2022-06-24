package com.aecb.data.api.models.request.updateuserprofile;

public class UpdateUserProfileRequest {

    private int updateType;
    private float tcVersion;
    private boolean newsLetterSubscription = false;
    private int channel;
    private String deviceId;
    private String email;
    private String preferredlanguage;
    private String mobile;
    private String fullName;
    private String nationality;
    private String passport;
    private String preferredPaymentMethod;

    public String getPreferredPaymentMethod() {
        return preferredPaymentMethod;
    }

    public void setPreferredPaymentMethod(String preferredPaymentMethod) {
        this.preferredPaymentMethod = preferredPaymentMethod;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreferredlanguage() {
        return preferredlanguage;
    }

    public void setPreferredlanguage(String preferredlanguage) {
        this.preferredlanguage = preferredlanguage;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    public float getTcVersion() {
        return tcVersion;
    }

    public void setTcVersion(float tcVersion) {
        this.tcVersion = tcVersion;
    }

    public boolean isNewsLetterSubscription() {
        return newsLetterSubscription;
    }

    public void setNewsLetterSubscription(boolean newsLetterSubscription) {
        this.newsLetterSubscription = newsLetterSubscription;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getNationalityId() {
        return nationality;
    }

    public void setNationalityId(String nationalityID) {
        this.nationality = nationalityID;
    }

}
