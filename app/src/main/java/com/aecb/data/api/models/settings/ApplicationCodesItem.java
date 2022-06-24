package com.aecb.data.api.models.settings;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ApplicationCodesItem {

	@SerializedName("code")
	private String code;

	@SerializedName("messageAr")
	private String messageAr;

	@SerializedName("messageEn")
	private String messageEn;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessageAr() {
		return messageAr;
	}

	public void setMessageAr(String messageAr) {
		this.messageAr = messageAr;
	}

	public String getMessageEn() {
		return messageEn;
	}

	public void setMessageEn(String messageEn) {
		this.messageEn = messageEn;
	}

    @Override
    public String toString() {
        return
                "ApplicationCodesItem{" +
                        "code = '" + code + '\'' +
                        ",messageAr = '" + messageAr + '\'' +
                        ",messageEn = '" + messageEn + '\'' +
                        "}";
    }
}