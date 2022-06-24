package com.aecb.data.api.models.response.login;

import com.aecb.data.api.models.login.ParseUser;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class LoginData {

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

    @SerializedName("portalUserId")
    private String portalUserId;

    @SerializedName("noScore")
    private boolean noScore;

    @SerializedName("parseUser")
    private ParseUser parseUser;

    public void setParseUser(ParseUser parseUser){
        this.parseUser = parseUser;
    }

    public ParseUser getParseUser(){
        return parseUser;
    }

    public String getPortalUserId() {
        return portalUserId;
    }

    public void setPortalUserId(String portalUserId) {
        this.portalUserId = portalUserId;
    }

    public boolean isNoScore() {
        return noScore;
    }

    public void setNoScore(boolean noScore) {
        this.noScore = noScore;
    }

    public void setCrmID(String crmID) {
        this.crmID = crmID;
    }

    public String getCrmID() {
        return crmID;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setHasAcceptedLatestTCs(boolean hasAcceptedLatestTCs) {
        this.hasAcceptedLatestTCs = hasAcceptedLatestTCs;
    }

    public boolean isHasAcceptedLatestTCs() {
        return hasAcceptedLatestTCs;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
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