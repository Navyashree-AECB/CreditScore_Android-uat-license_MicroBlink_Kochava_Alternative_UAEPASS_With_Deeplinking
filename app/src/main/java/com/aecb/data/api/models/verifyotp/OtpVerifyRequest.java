package com.aecb.data.api.models.verifyotp;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class OtpVerifyRequest {

	@SerializedName("otpToken")
	private String otpToken;

	@SerializedName("otpValue")
	private String otpValue;

	@SerializedName("otpType")
	private String otpType;

	public String getOtpToken() {
		return otpToken;
	}

	public void setOtpToken(String otpToken) {
		this.otpToken = otpToken;
	}

	public String getOtpValue() {
		return otpValue;
	}

	public void setOtpValue(String otpValue) {
		this.otpValue = otpValue;
	}

	public String getOtpType() {
		return otpType;
	}

	public void setOtpType(String otpType) {
		this.otpType = otpType;
	}

	@Override
	public String toString() {
		return
				"OtpVerifyRequest{" +
						"otpToken = '" + otpToken + '\'' +
						",otpValue = '" + otpValue + '\'' +
						",otpType = '" + otpType + '\'' +
						"}";
	}
}