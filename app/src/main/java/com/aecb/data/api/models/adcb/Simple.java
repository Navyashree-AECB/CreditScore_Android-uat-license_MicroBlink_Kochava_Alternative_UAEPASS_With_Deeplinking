package com.aecb.data.api.models.adcb;

import com.google.gson.annotations.SerializedName;

public class Simple{

	@SerializedName("htmlBodyContent")
	private String htmlBodyContent;

	public void setHtmlBodyContent(String htmlBodyContent){
		this.htmlBodyContent = htmlBodyContent;
	}

	public String getHtmlBodyContent(){
		return htmlBodyContent;
	}
}