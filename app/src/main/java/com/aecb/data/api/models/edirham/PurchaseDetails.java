package com.aecb.data.api.models.edirham;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PurchaseDetails{

	@SerializedName("service")
	private List<ServiceItem> service;

	public void setService(List<ServiceItem> service){
		this.service = service;
	}

	public List<ServiceItem> getService(){
		return service;
	}
}