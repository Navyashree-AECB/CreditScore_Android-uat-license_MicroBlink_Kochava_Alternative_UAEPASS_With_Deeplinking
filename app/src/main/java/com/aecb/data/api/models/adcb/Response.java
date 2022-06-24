package com.aecb.data.api.models.adcb;

import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("acquirerMessage")
	private String acquirerMessage;

	@SerializedName("gatewayCode")
	private String gatewayCode;

	@SerializedName("acquirerCode")
	private String acquirerCode;

	@SerializedName("gatewayRecommendation")
	private String gatewayRecommendation;

	public void setGatewayRecommendation(String gatewayRecommendation){
		this.gatewayRecommendation = gatewayRecommendation;
	}

	public String getGatewayRecommendation(){
		return gatewayRecommendation;
	}

	public void setAcquirerMessage(String acquirerMessage){
		this.acquirerMessage = acquirerMessage;
	}

	public String getAcquirerMessage(){
		return acquirerMessage;
	}

	public void setGatewayCode(String gatewayCode){
		this.gatewayCode = gatewayCode;
	}

	public String getGatewayCode(){
		return gatewayCode;
	}

	public void setAcquirerCode(String acquirerCode){
		this.acquirerCode = acquirerCode;
	}

	public String getAcquirerCode(){
		return acquirerCode;
	}
}