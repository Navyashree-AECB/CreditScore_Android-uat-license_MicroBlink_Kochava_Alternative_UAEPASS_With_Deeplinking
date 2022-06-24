package com.aecb.data.api.models.edirham;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PurchaseCheckoutRequest{

	@SerializedName("password")
	private String password;

	@SerializedName("orderNumber")
	private String orderNumber;

	@SerializedName("sessionTimeoutSecs")
	private String sessionTimeoutSecs;

	@SerializedName("channel")
	private String channel;

	@SerializedName("addTransactionFeesOnTop")
	private boolean addTransactionFeesOnTop;

	@SerializedName("merchantSiteUrl")
	private String merchantSiteUrl;

	@SerializedName("language")
	private String language;

	@SerializedName("governmentServices")
	private boolean governmentServices;

	@SerializedName("purchaseDetails")
	private PurchaseDetails purchaseDetails;

	@SerializedName("userName")
	private String userName;

	@SerializedName("paymentMethodList")
	private List<String> paymentMethodList;

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setOrderNumber(String orderNumber){
		this.orderNumber = orderNumber;
	}

	public String getOrderNumber(){
		return orderNumber;
	}

	public void setSessionTimeoutSecs(String sessionTimeoutSecs){
		this.sessionTimeoutSecs = sessionTimeoutSecs;
	}

	public String getSessionTimeoutSecs(){
		return sessionTimeoutSecs;
	}

	public void setChannel(String channel){
		this.channel = channel;
	}

	public String getChannel(){
		return channel;
	}

	public void setAddTransactionFeesOnTop(boolean addTransactionFeesOnTop){
		this.addTransactionFeesOnTop = addTransactionFeesOnTop;
	}

	public boolean isAddTransactionFeesOnTop(){
		return addTransactionFeesOnTop;
	}

	public void setMerchantSiteUrl(String merchantSiteUrl){
		this.merchantSiteUrl = merchantSiteUrl;
	}

	public String getMerchantSiteUrl(){
		return merchantSiteUrl;
	}

	public void setLanguage(String language){
		this.language = language;
	}

	public String getLanguage(){
		return language;
	}

	public void setGovernmentServices(boolean governmentServices){
		this.governmentServices = governmentServices;
	}

	public boolean isGovernmentServices(){
		return governmentServices;
	}

	public void setPurchaseDetails(PurchaseDetails purchaseDetails){
		this.purchaseDetails = purchaseDetails;
	}

	public PurchaseDetails getPurchaseDetails(){
		return purchaseDetails;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setPaymentMethodList(List<String> paymentMethodList){
		this.paymentMethodList = paymentMethodList;
	}

	public List<String> getPaymentMethodList(){
		return paymentMethodList;
	}
}