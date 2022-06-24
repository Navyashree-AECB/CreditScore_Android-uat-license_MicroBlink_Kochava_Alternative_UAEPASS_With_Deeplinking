package com.aecb.data.api.models.generateotp;

import com.google.gson.annotations.SerializedName;

public class GenerateOtpRequest {
    @SerializedName("portalId")
    private String portalId;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("referenceId")
    private String referenceId;

    @SerializedName("username")
    private String userName;

    @SerializedName("requestOtpFor")
    private String requestOtpFor;

    @SerializedName("otpToken")
    private String otpToken;

    public String getOtpToken() {
        return otpToken;
    }

    public void setOtpToken(String otpToken) {
        this.otpToken = otpToken;
    }

    public String getPortalId() {
        return portalId;
    }

    public void setPortalId(String portalId) {
        this.portalId = portalId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getRequestOtpFor() {
        return requestOtpFor;
    }

    public void setRequestOtpFor(String requestOtpFor) {
        this.requestOtpFor = requestOtpFor;
    }

    @Override
    public String toString() {
        return
                "Response{" +
                        "portalId = '" + portalId + '\'' +
                        ",mobile = '" + mobile + '\'' +
                        ",referenceId = '" + referenceId + '\'' +
                        ",username = '" + userName + '\'' +
                        ",requestOtpFor = '" + requestOtpFor + '\'' +
                        "}";
    }
}