package com.aecb.data.api.models.adcb;

import com.google.gson.annotations.SerializedName;

public class Chargeback{

	@SerializedName("amount")
	private Integer amount;

	@SerializedName("currency")
	private String currency;

	public void setAmount(Integer amount){
		this.amount = amount;
	}

	public Integer getAmount(){
		return amount;
	}

	public void setCurrency(String currency){
		this.currency = currency;
	}

	public String getCurrency(){
		return currency;
	}
}