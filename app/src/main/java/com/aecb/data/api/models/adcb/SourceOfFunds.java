package com.aecb.data.api.models.adcb;

import com.google.gson.annotations.SerializedName;

public class SourceOfFunds{

	@SerializedName("provided")
	private Provided provided;

	@SerializedName("type")
	private String type;

	@SerializedName("token")
	private String token;

	public void setProvided(Provided provided){
		this.provided = provided;
	}

	public Provided getProvided(){
		return provided;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}
}