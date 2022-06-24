package com.aecb.data.api.models.adcb;

import com.google.gson.annotations.SerializedName;

public class Transaction{

	@SerializedName("amount")
	private Double amount;

	@SerializedName("authorizationCode")
	private String authorizationCode;

	@SerializedName("currency")
	private String currency;

	@SerializedName("receipt")
	private String receipt;

	@SerializedName("id")
	private String id;

	@SerializedName("source")
	private String source;

	@SerializedName("terminal")
	private String terminal;

	@SerializedName("acquirer")
	private Acquirer acquirer;

	@SerializedName("type")
	private String type;

	@SerializedName("frequency")
	private String frequency;

	public void setAmount(Double amount){
		this.amount = amount;
	}

	public Double getAmount(){
		return amount;
	}

	public void setAuthorizationCode(String authorizationCode){
		this.authorizationCode = authorizationCode;
	}

	public String getAuthorizationCode(){
		return authorizationCode;
	}

	public void setCurrency(String currency){
		this.currency = currency;
	}

	public String getCurrency(){
		return currency;
	}

	public void setReceipt(String receipt){
		this.receipt = receipt;
	}

	public String getReceipt(){
		return receipt;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setSource(String source){
		this.source = source;
	}

	public String getSource(){
		return source;
	}

	public void setTerminal(String terminal){
		this.terminal = terminal;
	}

	public String getTerminal(){
		return terminal;
	}

	public void setAcquirer(Acquirer acquirer){
		this.acquirer = acquirer;
	}

	public Acquirer getAcquirer(){
		return acquirer;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setFrequency(String frequency){
		this.frequency = frequency;
	}

	public String getFrequency(){
		return frequency;
	}
}