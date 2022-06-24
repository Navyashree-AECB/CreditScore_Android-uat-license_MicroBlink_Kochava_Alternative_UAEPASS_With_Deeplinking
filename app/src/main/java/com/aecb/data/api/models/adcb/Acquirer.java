package com.aecb.data.api.models.adcb;

import com.google.gson.annotations.SerializedName;

public class Acquirer{

	@SerializedName("date")
	private String date;

	@SerializedName("merchantId")
	private String merchantId;

	@SerializedName("batch")
	private Integer batch;

	@SerializedName("timeZone")
	private String timeZone;

	@SerializedName("id")
	private String id;

	@SerializedName("settlementDate")
	private String settlementDate;

	@SerializedName("transactionId")
	private String transactionId;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setMerchantId(String merchantId){
		this.merchantId = merchantId;
	}

	public String getMerchantId(){
		return merchantId;
	}

	public void setBatch(Integer batch){
		this.batch = batch;
	}

	public Integer getBatch(){
		return batch;
	}

	public void setTimeZone(String timeZone){
		this.timeZone = timeZone;
	}

	public String getTimeZone(){
		return timeZone;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setSettlementDate(String settlementDate){
		this.settlementDate = settlementDate;
	}

	public String getSettlementDate(){
		return settlementDate;
	}

	public void setTransactionId(String transactionId){
		this.transactionId = transactionId;
	}

	public String getTransactionId(){
		return transactionId;
	}
}