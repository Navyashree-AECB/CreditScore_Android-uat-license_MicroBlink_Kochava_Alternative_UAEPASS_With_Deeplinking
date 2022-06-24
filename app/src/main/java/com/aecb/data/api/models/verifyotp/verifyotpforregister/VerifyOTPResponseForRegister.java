package com.aecb.data.api.models.verifyotp.verifyotpforregister;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class VerifyOTPResponseForRegister {

    @SerializedName("parseUser")
    private ParseUser parseUser;

    @SerializedName("noScore")
    private boolean noScore;

    @SerializedName("portalUserId")
    private String portalUserId;

    public void setParseUser(ParseUser parseUser) {
        this.parseUser = parseUser;
    }

    public ParseUser getParseUser() {
        return parseUser;
    }

    public void setNoScore(boolean noScore) {
        this.noScore = noScore;
    }

    public boolean isNoScore() {
        return noScore;
    }

    public void setPortalUserId(String portalUserId) {
        this.portalUserId = portalUserId;
    }

    public String getPortalUserId() {
        return portalUserId;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "parseUser = '" + parseUser + '\'' +
                        ",noScore = '" + noScore + '\'' +
                        ",portalUserId = '" + portalUserId + '\'' +
                        "}";
    }
}