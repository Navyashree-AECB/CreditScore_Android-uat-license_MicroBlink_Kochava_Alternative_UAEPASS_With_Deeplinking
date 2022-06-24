package com.aecb.data.api.models.payment_method;

import com.aecb.data.api.models.BaseResponse;
import com.google.gson.annotations.SerializedName;

public class AddCardResponse extends BaseResponse {

    @SerializedName("data")
    private AddCardResponseItems cardResponseItems;

    public AddCardResponseItems getCardResponseItems() {
        return cardResponseItems;
    }

    public void setCardResponseItems(AddCardResponseItems cardResponseItems) {
        this.cardResponseItems = cardResponseItems;
    }
}