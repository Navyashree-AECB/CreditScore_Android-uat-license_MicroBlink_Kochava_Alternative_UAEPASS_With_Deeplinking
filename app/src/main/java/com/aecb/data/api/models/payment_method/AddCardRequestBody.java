package com.aecb.data.api.models.payment_method;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class AddCardRequestBody {

    @SerializedName("pymtCardIsDefault")
    private String pymtCardIsDefault;

    @SerializedName("amount")
    private String amount;

    @SerializedName("cvv")
    private String cvv;

    @SerializedName("cardholderName")
    private String cardholderName;

    @SerializedName("cardType")
    private String cardType;

    @SerializedName("cardIssuingBank")
    private String cardIssuingBank;

    @SerializedName("operationType")
    private String operationType;

    @SerializedName("expiry")
    private String expiry;

    @SerializedName("cardNumber")
    private String cardNumber;

    @SerializedName("transactionID")
    private String transactionID;

    @SerializedName("consent")
    private String consent;

    public String getConsent() {
        return consent;
    }

    public void setConsent(String consent) {
        this.consent = consent;
    }

    public String getPymtCardIsDefault() {
        return pymtCardIsDefault;
    }

    public void setPymtCardIsDefault(String pymtCardIsDefault) {
        this.pymtCardIsDefault = pymtCardIsDefault;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardIssuingBank() {
        return cardIssuingBank;
    }

    public void setCardIssuingBank(String cardIssuingBank) {
        this.cardIssuingBank = cardIssuingBank;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    @Override
    public String toString() {

        //Todo confirm the request from backend and then remove this code
       /* if (cvv == null && consent == null) { //old card
            return "{" +
                    "\"operationType\":\"" + operationType + "\"," +
                    "\"amount\":\"" + amount + "\"," +
                    "\"cardNumber\":\"" + cardNumber + "\"," +
                    "\"expiry\":\"" + expiry + "\"," +
                    "\"cardholderName\":\"" + cardholderName + "\"," +
                    "\"transactionID\":\"" + transactionID + "\"," +
                    "\"pymtCardIsDefault\":\"" + pymtCardIsDefault + "\"" +
                    "}";
       *//* } else if (amount == null && transactionID == null) { // config new card
            return "{" +
                    "\"operationType\":\"" + operationType + "\"," +
                    "\"cardNumber\":\"" + cardNumber + "\"," +
                    "\"expiry\":\"" + expiry + "\"," +
                    "\"cardholderName\":\"" + cardholderName + "\"," +
                    "\"cvv\":\"" + cvv + "\"," +
                    "\"consent\":\"" + consent + "\"," +
                    "\"pymtCardIsDefault\":\"" + pymtCardIsDefault + "\"" +
                    "}";*//*
        } else {// new card*/

        if (cvv == null) {
            return "{" +
                    "\"operationType\":\"" + operationType + "\"," +
                    "\"amount\":\"" + amount + "\"," +
                    "\"cardNumber\":\"" + cardNumber + "\"," +
                    "\"expiry\":\"" + expiry + "\"," +
                    "\"cardholderName\":\"" + cardholderName + "\"," +
                    "\"transactionID\":\"" + transactionID + "\"," +
                    "\"consent\":\"" + consent + "\"," +
                    "\"pymtCardIsDefault\":\"" + pymtCardIsDefault + "\"" +
                    "}";
        } else {
            return "{" +
                    "\"operationType\":\"" + operationType + "\"," +
                    "\"amount\":\"" + amount + "\"," +
                    "\"cardNumber\":\"" + cardNumber + "\"," +
                    "\"expiry\":\"" + expiry + "\"," +
                    "\"cvv\":\"" + cvv + "\"," +
                    "\"cardholderName\":\"" + cardholderName + "\"," +
                    "\"transactionID\":\"" + transactionID + "\"," +
                    "\"consent\":\"" + consent + "\"," +
                    "\"pymtCardIsDefault\":\"" + pymtCardIsDefault + "\"" +
                    "}";
        }
        //  }
    }
}