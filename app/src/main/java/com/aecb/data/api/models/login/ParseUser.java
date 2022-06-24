package com.aecb.data.api.models.login;

import com.google.gson.annotations.SerializedName;

public class ParseUser {

    @SerializedName("crmID")
    private String crmID;

    @SerializedName("noScore")
    private boolean noScore;

    @SerializedName("sessionToken")
    private String sessionToken;

    @SerializedName("email")
    private String email;

    public void setCrmID(String crmID) {
        this.crmID = crmID;
    }

    public String getCrmID() {
        return crmID;
    }

    public void setNoScore(boolean noScore) {
        this.noScore = noScore;
    }

    public boolean isNoScore() {
        return noScore;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}