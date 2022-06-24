package com.aecb.data.api.models.payment_method;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ActivateCardRequestBody {

    @SerializedName("amount")
    private String amount;

    @SerializedName("operationType")
    private String operationType;

    @SerializedName("consent")
    private String consent;

    @SerializedName("transactionID")
    private String transactionID;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getConsent() {
        return consent;
    }

    public void setConsent(String consent) {
        this.consent = consent;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    @Override
    public String toString() {
        return "{" +
                "\"amount\":" + amount + "," +
                "\"operationType\":\"" + operationType + "\"," +
                "\"transactionID\":\"" + transactionID + "\"," +
                "\"consent\":\"" + consent + "\"" +
                "}";
    }
}