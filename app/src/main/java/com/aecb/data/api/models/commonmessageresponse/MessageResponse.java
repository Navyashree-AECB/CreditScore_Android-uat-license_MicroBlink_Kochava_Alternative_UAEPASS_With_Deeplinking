package com.aecb.data.api.models.commonmessageresponse;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MessageResponse{

	@SerializedName("message")
	private List<MessageItem> message;

	public void setMessage(List<MessageItem> message){
		this.message = message;
	}

	public List<MessageItem> getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"MessageResponse{" + 
			"message = '" + message + '\'' + 
			"}";
		}
}