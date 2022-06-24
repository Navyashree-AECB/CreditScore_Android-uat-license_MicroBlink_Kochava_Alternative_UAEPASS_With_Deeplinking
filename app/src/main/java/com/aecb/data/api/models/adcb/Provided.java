package com.aecb.data.api.models.adcb;

import com.google.gson.annotations.SerializedName;

public class Provided{

	@SerializedName("card")
	private Card card;

	public void setCard(Card card){
		this.card = card;
	}

	public Card getCard(){
		return card;
	}
}