package com.aecb.data.api.models.verifyotp;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class VerifyOtpData {

    @SerializedName("crmID")
    private String crmID;

    @SerializedName("preferredLanguage")
    private String preferredLanguage;

    @SerializedName("sessionToken")
    private String sessionToken;

    @SerializedName("hasAcceptedLatestTCs")
    private boolean hasAcceptedLatestTCs;

    @SerializedName("email")
    private String email;

    public String getCrmID() {
        return crmID;
    }

    public void setCrmID(String crmID) {
        this.crmID = crmID;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public boolean isHasAcceptedLatestTCs() {
        return hasAcceptedLatestTCs;
    }

    public void setHasAcceptedLatestTCs(boolean hasAcceptedLatestTCs) {
        this.hasAcceptedLatestTCs = hasAcceptedLatestTCs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "crmID = '" + crmID + '\'' +
                        ",preferredLanguage = '" + preferredLanguage + '\'' +
                        ",sessionToken = '" + sessionToken + '\'' +
                        ",hasAcceptedLatestTCs = '" + hasAcceptedLatestTCs + '\'' +
                        ",email = '" + email + '\'' +
                        "}";
    }
}