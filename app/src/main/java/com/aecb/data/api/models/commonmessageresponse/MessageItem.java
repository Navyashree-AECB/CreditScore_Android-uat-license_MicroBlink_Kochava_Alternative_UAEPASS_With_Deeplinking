package com.aecb.data.api.models.commonmessageresponse;

import com.google.gson.annotations.SerializedName;

public class MessageItem{

	@SerializedName("use_case")
	private String useCase;

	@SerializedName("message_ar")
	private String messageAr;

	@SerializedName("message_en")
	private String messageEn;

	@SerializedName("status")
	private String status;

	public void setUseCase(String useCase){
		this.useCase = useCase;
	}

	public String getUseCase(){
		return useCase;
	}

	public void setMessageAr(String messageAr){
		this.messageAr = messageAr;
	}

	public String getMessageAr(){
		return messageAr;
	}

	public void setMessageEn(String messageEn){
		this.messageEn = messageEn;
	}

	public String getMessageEn(){
		return messageEn;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"MessageItem{" + 
			"use_case = '" + useCase + '\'' + 
			",message_ar = '" + messageAr + '\'' + 
			",message_en = '" + messageEn + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}