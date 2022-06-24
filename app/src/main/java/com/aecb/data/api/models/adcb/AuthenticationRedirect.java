package com.aecb.data.api.models.adcb;

import com.google.gson.annotations.SerializedName;

public class AuthenticationRedirect{

	@SerializedName("simple")
	private Simple simple;

	public void setSimple(Simple simple){
		this.simple = simple;
	}

	public Simple getSimple(){
		return simple;
	}
}