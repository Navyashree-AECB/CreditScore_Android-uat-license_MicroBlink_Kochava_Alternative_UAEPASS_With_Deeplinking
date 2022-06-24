package com.aecb.data.api.models.adcb;

import com.google.gson.annotations.SerializedName;

public class Card{

	@SerializedName("number")
	private String number;

	@SerializedName("scheme")
	private String scheme;

	@SerializedName("fundingMethod")
	private String fundingMethod;

	@SerializedName("expiry")
	private Expiry expiry;

	@SerializedName("brand")
	private String brand;

	public void setNumber(String number){
		this.number = number;
	}

	public String getNumber(){
		return number;
	}

	public void setScheme(String scheme){
		this.scheme = scheme;
	}

	public String getScheme(){
		return scheme;
	}

	public void setFundingMethod(String fundingMethod){
		this.fundingMethod = fundingMethod;
	}

	public String getFundingMethod(){
		return fundingMethod;
	}

	public void setExpiry(Expiry expiry){
		this.expiry = expiry;
	}

	public Expiry getExpiry(){
		return expiry;
	}

	public void setBrand(String brand){
		this.brand = brand;
	}

	public String getBrand(){
		return brand;
	}
}