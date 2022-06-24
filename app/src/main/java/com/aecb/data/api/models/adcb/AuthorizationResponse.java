package com.aecb.data.api.models.adcb;

import com.google.gson.annotations.SerializedName;

public class AuthorizationResponse{

	@SerializedName("date")
	private String date;

	@SerializedName("financialNetworkCode")
	private String financialNetworkCode;

	@SerializedName("posData")
	private String posData;

	@SerializedName("processingCode")
	private String processingCode;

	@SerializedName("stan")
	private String stan;

	@SerializedName("transactionIdentifier")
	private String transactionIdentifier;

	@SerializedName("commercialCardIndicator")
	private String commercialCardIndicator;

	@SerializedName("posEntryMode")
	private String posEntryMode;

	@SerializedName("time")
	private String time;

	@SerializedName("responseCode")
	private String responseCode;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setFinancialNetworkCode(String financialNetworkCode){
		this.financialNetworkCode = financialNetworkCode;
	}

	public String getFinancialNetworkCode(){
		return financialNetworkCode;
	}

	public void setPosData(String posData){
		this.posData = posData;
	}

	public String getPosData(){
		return posData;
	}

	public void setProcessingCode(String processingCode){
		this.processingCode = processingCode;
	}

	public String getProcessingCode(){
		return processingCode;
	}

	public void setStan(String stan){
		this.stan = stan;
	}

	public String getStan(){
		return stan;
	}

	public void setTransactionIdentifier(String transactionIdentifier){
		this.transactionIdentifier = transactionIdentifier;
	}

	public String getTransactionIdentifier(){
		return transactionIdentifier;
	}

	public void setCommercialCardIndicator(String commercialCardIndicator){
		this.commercialCardIndicator = commercialCardIndicator;
	}

	public String getCommercialCardIndicator(){
		return commercialCardIndicator;
	}

	public void setPosEntryMode(String posEntryMode){
		this.posEntryMode = posEntryMode;
	}

	public String getPosEntryMode(){
		return posEntryMode;
	}

	public void setTime(String time){
		this.time = time;
	}

	public String getTime(){
		return time;
	}

	public void setResponseCode(String responseCode){
		this.responseCode = responseCode;
	}

	public String getResponseCode(){
		return responseCode;
	}
}