package com.aecb.data.api.models.adcb;

import com.google.gson.annotations.SerializedName;

public class Order{

	@SerializedName("amount")
	private Double amount;

	@SerializedName("creationTime")
	private String creationTime;

	@SerializedName("chargeback")
	private Chargeback chargeback;

	@SerializedName("merchantCategoryCode")
	private String merchantCategoryCode;

	@SerializedName("totalCapturedAmount")
	private Double totalCapturedAmount;

	@SerializedName("currency")
	private String currency;

	@SerializedName("id")
	private String id;

	@SerializedName("totalAuthorizedAmount")
	private Double totalAuthorizedAmount;

	@SerializedName("status")
	private String status;

	@SerializedName("totalRefundedAmount")
	private Double totalRefundedAmount;

	public void setAmount(Double amount){
		this.amount = amount;
	}

	public Double getAmount(){
		return amount;
	}

	public void setCreationTime(String creationTime){
		this.creationTime = creationTime;
	}

	public String getCreationTime(){
		return creationTime;
	}

	public void setChargeback(Chargeback chargeback){
		this.chargeback = chargeback;
	}

	public Chargeback getChargeback(){
		return chargeback;
	}

	public void setMerchantCategoryCode(String merchantCategoryCode){
		this.merchantCategoryCode = merchantCategoryCode;
	}

	public String getMerchantCategoryCode(){
		return merchantCategoryCode;
	}

	public void setTotalCapturedAmount(Double totalCapturedAmount){
		this.totalCapturedAmount = totalCapturedAmount;
	}

	public Double getTotalCapturedAmount(){
		return totalCapturedAmount;
	}

	public void setCurrency(String currency){
		this.currency = currency;
	}

	public String getCurrency(){
		return currency;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTotalAuthorizedAmount(Double totalAuthorizedAmount){
		this.totalAuthorizedAmount = totalAuthorizedAmount;
	}

	public Double getTotalAuthorizedAmount(){
		return totalAuthorizedAmount;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setTotalRefundedAmount(Double totalRefundedAmount){
		this.totalRefundedAmount = totalRefundedAmount;
	}

	public Double getTotalRefundedAmount(){
		return totalRefundedAmount;
	}
}