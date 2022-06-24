package com.aecb.data.api.models.payment_method;

import com.google.gson.annotations.SerializedName;

public class DeleteCardRequestBody {

    @SerializedName("cardNumber")
    private String cardNumber;

    @SerializedName("expiry")
    private String expiry;

    @SerializedName("operationType")
    private String operationType;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    @Override
    public String toString() {
        return "{" +
                "\"cardNumber\":\"" + cardNumber + "\"," +
                "\"expiry\":\"" + expiry + "\"," +
                "\"operationType\":\"" + operationType + "\"" +
                "}";
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}