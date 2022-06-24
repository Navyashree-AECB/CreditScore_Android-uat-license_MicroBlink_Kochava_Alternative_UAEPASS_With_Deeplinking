package com.aecb.data.api.models.cards;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreditCardList {

    @SerializedName("data")
    private List<CreditCardData> creditCardDataList;

    public List<CreditCardData> getcreditCardDataList() {
        return creditCardDataList;
    }

    public void setCreditCardDataList(List<CreditCardData> creditCardDataList) {
        this.creditCardDataList = creditCardDataList;
    }

}