package com.aecb.data.api.models.edirham;

import com.google.gson.annotations.SerializedName;

public class ServiceItem{

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("serviceCode")
	private String serviceCode;

	@SerializedName("numberOfUnits")
	private int numberOfUnits;

	@SerializedName("transactionAmount")
	private double transactionAmount;

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public int getQuantity(){
		return quantity;
	}

	public void setServiceCode(String serviceCode){
		this.serviceCode = serviceCode;
	}

	public String getServiceCode(){
		return serviceCode;
	}

	public void setNumberOfUnits(int numberOfUnits){
		this.numberOfUnits = numberOfUnits;
	}

	public int getNumberOfUnits(){
		return numberOfUnits;
	}

	public void setTransactionAmount(double transactionAmount){
		this.transactionAmount = transactionAmount;
	}

	public double getTransactionAmount(){
		return transactionAmount;
	}
}