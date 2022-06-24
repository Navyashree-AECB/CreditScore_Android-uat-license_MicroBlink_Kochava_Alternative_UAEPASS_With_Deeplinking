package com.aecb.data.api.models.edirham;

import com.google.gson.annotations.SerializedName;

public class CheckoutStatusResponse{

	@SerializedName("urn")
	private String urn;

	@SerializedName("orderNumber")
	private String orderNumber;

	@SerializedName("orderID")
	private String orderID;

	@SerializedName("settlementExpiry")
	private Object settlementExpiry;

	@SerializedName("errorMessage")
	private String errorMessage;

	@SerializedName("orderStatus")
	private int orderStatus;

	@SerializedName("checkoutId")
	private String checkoutId;

	@SerializedName("checkoutStatus")
	private String checkoutStatus;

	@SerializedName("responseCode")
	private int responseCode;

	public void setUrn(String urn){
		this.urn = urn;
	}

	public String getUrn(){
		return urn;
	}

	public void setOrderNumber(String orderNumber){
		this.orderNumber = orderNumber;
	}

	public String getOrderNumber(){
		return orderNumber;
	}

	public void setOrderID(String orderID){
		this.orderID = orderID;
	}

	public String getOrderID(){
		return orderID;
	}

	public void setSettlementExpiry(Object settlementExpiry){
		this.settlementExpiry = settlementExpiry;
	}

	public Object getSettlementExpiry(){
		return settlementExpiry;
	}

	public void setErrorMessage(String errorMessage){
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage(){
		return errorMessage;
	}

	public void setOrderStatus(int orderStatus){
		this.orderStatus = orderStatus;
	}

	public int getOrderStatus(){
		return orderStatus;
	}

	public void setCheckoutId(String checkoutId){
		this.checkoutId = checkoutId;
	}

	public String getCheckoutId(){
		return checkoutId;
	}

	public void setCheckoutStatus(String checkoutStatus){
		this.checkoutStatus = checkoutStatus;
	}

	public String getCheckoutStatus(){
		return checkoutStatus;
	}

	public void setResponseCode(int responseCode){
		this.responseCode = responseCode;
	}

	public int getResponseCode(){
		return responseCode;
	}
}