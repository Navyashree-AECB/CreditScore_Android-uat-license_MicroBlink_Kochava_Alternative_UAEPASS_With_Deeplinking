package com.aecb.data.api.models.adcb;

import com.google.gson.annotations.SerializedName;

public class JsonMember3DSecure {

    @SerializedName("paResStatus")
    private String paResStatus;

    @SerializedName("xid")
    private String xid;

    @SerializedName("acsEci")
    private String acsEci;

    @SerializedName("authenticationToken")
    private String authenticationToken;

    @SerializedName("veResEnrolled")
    private String veResEnrolled;

    @SerializedName("authenticationRedirect")
    private AuthenticationRedirect authenticationRedirect;

    public AuthenticationRedirect getAuthenticationRedirect() {
        return authenticationRedirect;
    }

    public void setAuthenticationRedirect(AuthenticationRedirect authenticationRedirect) {
        this.authenticationRedirect = authenticationRedirect;
    }

    public void setPaResStatus(String paResStatus) {
        this.paResStatus = paResStatus;
    }

    public String getPaResStatus() {
        return paResStatus;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public String getXid() {
        return xid;
    }

    public void setAcsEci(String acsEci) {
        this.acsEci = acsEci;
    }

    public String getAcsEci() {
        return acsEci;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setVeResEnrolled(String veResEnrolled) {
        this.veResEnrolled = veResEnrolled;
    }

    public String getVeResEnrolled() {
        return veResEnrolled;
    }
}