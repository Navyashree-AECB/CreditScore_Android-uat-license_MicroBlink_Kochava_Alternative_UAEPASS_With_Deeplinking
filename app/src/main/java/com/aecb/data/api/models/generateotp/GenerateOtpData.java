package com.aecb.data.api.models.generateotp;

import com.aecb.data.api.models.BaseResponse;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class GenerateOtpData extends BaseResponse {

    @SerializedName("canVerify")
    private boolean canVerify;

    @SerializedName("canResend")
    private boolean canResend;

    @SerializedName("noScore")
    private boolean noScore;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("portalUserId")
    private String portalUserId;

    @SerializedName("otpToken")
    private String otpToken;

    @SerializedName("remainingAttempts")
    private int remainingAttempts;

    public boolean isCanVerify() {
        return canVerify;
    }

    public void setCanVerify(boolean canVerify) {
        this.canVerify = canVerify;
    }

    public boolean isCanResend() {
        return canResend;
    }

    public void setCanResend(boolean canResend) {
        this.canResend = canResend;
    }

    public boolean isNoScore() {
        return noScore;
    }

    public void setNoScore(boolean noScore) {
        this.noScore = noScore;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPortalUserId() {
        return portalUserId;
    }

    public void setPortalUserId(String portalUserId) {
        this.portalUserId = portalUserId;
    }

    public String getOtpToken() {
        return otpToken;
    }

    public void setOtpToken(String otpToken) {
        this.otpToken = otpToken;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public void setRemainingAttempts(int remainingAttempts) {
        this.remainingAttempts = remainingAttempts;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "canVerify = '" + canVerify + '\'' +
                        ",canResend = '" + canResend + '\'' +
                        ",noScore = '" + noScore + '\'' +
                        ",mobile = '" + mobile + '\'' +
                        ",portalUserId = '" + portalUserId + '\'' +
                        ",otpToken = '" + otpToken + '\'' +
                        ",remainingAttempts = '" + remainingAttempts + '\'' +
                        "}";
    }
}