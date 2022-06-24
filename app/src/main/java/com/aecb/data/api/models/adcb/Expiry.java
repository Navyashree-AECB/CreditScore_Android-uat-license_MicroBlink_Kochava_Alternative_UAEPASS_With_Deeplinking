package com.aecb.data.api.models.adcb;

import com.google.gson.annotations.SerializedName;

public class Expiry{

	@SerializedName("month")
	private String month;

	@SerializedName("year")
	private String year;

	public void setMonth(String month){
		this.month = month;
	}

	public String getMonth(){
		return month;
	}

	public void setYear(String year){
		this.year = year;
	}

	public String getYear(){
		return year;
	}
}