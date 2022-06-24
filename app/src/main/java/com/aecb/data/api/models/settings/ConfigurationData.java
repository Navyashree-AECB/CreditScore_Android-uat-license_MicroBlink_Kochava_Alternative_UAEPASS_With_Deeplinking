package com.aecb.data.api.models.settings;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ConfigurationData {

    @SerializedName("isAppFree")
    public boolean isAppFree;
    @SerializedName("notificationIgnoreDuration")
    public int notificationIgnoreDuration;
    @SerializedName("notificationReminderLaterDuration")
    public int notificationReminderLaterDuration;
    @SerializedName("hearAboutUs")
    private Object hearAboutUs;
    @SerializedName("otpAliveTime")
    private Long otpAliveTime;
    @SerializedName("configurations")
    private List<ApplicationCodesItem> applicationCodes;
    @SerializedName("drupalBaseUrl")
    private String drupalBaseUrl;
    @SerializedName("caseTypes")
    private Object caseTypes;
    @SerializedName("caseOrigins")
    private Object caseOrigins;
    @SerializedName("refId")
    private String refId;
    @SerializedName("minIosAppVersion")
    private String minIosAppVersion;
    @SerializedName("minAndroidAppVersion")
    private String minAndroidAppVersion;

    public int getNotificationIgnoreDuration() {
        return notificationIgnoreDuration;
    }

    public void setNotificationIgnoreDuration(int notificationIgnoreDuration) {
        this.notificationIgnoreDuration = notificationIgnoreDuration;
    }

    public int getNotificationReminderLaterDuration() {
        return notificationReminderLaterDuration;
    }

    public void setNotificationReminderLaterDuration(int notificationReminderLaterDuration) {
        this.notificationReminderLaterDuration = notificationReminderLaterDuration;
    }

    public Object getHearAboutUs() {
        return hearAboutUs;
    }

    public void setHearAboutUs(Object hearAboutUs) {
        this.hearAboutUs = hearAboutUs;
    }

    public Long getOtpAliveTime() {
        return otpAliveTime;
    }

    public void setOtpAliveTime(Long otpAliveTime) {
        this.otpAliveTime = otpAliveTime;
    }

    public List<ApplicationCodesItem> getApplicationCodes() {
        return applicationCodes;
    }

    public void setApplicationCodes(List<ApplicationCodesItem> applicationCodes) {
        this.applicationCodes = applicationCodes;
    }

    public String getDrupalBaseUrl() {
        return drupalBaseUrl;
    }

    public void setDrupalBaseUrl(String drupalBaseUrl) {
        this.drupalBaseUrl = drupalBaseUrl;
    }

    public Object getCaseTypes() {
        return caseTypes;
    }

    public void setCaseTypes(Object caseTypes) {
        this.caseTypes = caseTypes;
    }

    public Object getCaseOrigins() {
        return caseOrigins;
    }

    public void setCaseOrigins(Object caseOrigins) {
        this.caseOrigins = caseOrigins;
    }

    public boolean isIsAppFree() {
        return isAppFree;
    }

    public void setIsAppFree(boolean isAppFree) {
        this.isAppFree = isAppFree;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getMinIosAppVersion() {
        return minIosAppVersion;
    }

    public void setMinIosAppVersion(String minIosAppVersion) {
        this.minIosAppVersion = minIosAppVersion;
    }

    public String getMinAndroidAppVersion() {
        return minAndroidAppVersion;
    }

    public void setMinAndroidAppVersion(String minAndroidAppVersion) {
        this.minAndroidAppVersion = minAndroidAppVersion;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        ",hearAboutUs = '" + hearAboutUs + '\'' +
                        ",otpAliveTime = '" + otpAliveTime + '\'' +
                        ",applicationCodes = '" + applicationCodes + '\'' +
                        ",drupalBaseUrl = '" + drupalBaseUrl + '\'' +
                        ",caseTypes = '" + caseTypes + '\'' +
                        ",caseOrigins = '" + caseOrigins + '\'' +
                        ",isAppFree = '" + isAppFree + '\'' +
                        ",refId = '" + refId + '\'' +
                        ",minIosAppVersion = '" + minIosAppVersion + '\'' +
                        ",minAndroidAppVersion = '" + minAndroidAppVersion + '\'' +
                        "}";
    }
}