package com.aecb.data.api.models.updatepaymentstatus;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class UpdatePaymentStatusItem {

    @SerializedName("paymentdate")
    private String paymentdate;

    @SerializedName("resultCode")
    private String resultCode;

    @SerializedName("resultMessage")
    private String resultMessage;

    @SerializedName("transactionID")
    private String transactionID;

    @SerializedName("paymentRef")
    private String paymentRef;

    @SerializedName("paymentSource")
    private String paymentSource;

    @SerializedName("paymentCheckoutId")
    private String paymentCheckoutId;

    public String getPaymentCheckoutId() {
        return paymentCheckoutId;
    }

    public void setPaymentCheckoutId(String paymentCheckoutId) {
        this.paymentCheckoutId = paymentCheckoutId;
    }

    public String getPaymentSource() {
        return paymentSource;
    }

    public void setPaymentSource(String paymentSource) {
        this.paymentSource = paymentSource;
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(String paymentdate) {
        this.paymentdate = paymentdate;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "paymentdate = '" + paymentdate + '\'' +
                        ",resultCode = '" + resultCode + '\'' +
                        ",resultMessage = '" + resultMessage + '\'' +
                        ",transactionID = '" + transactionID + '\'' +
                        ",paymentRef = '" + paymentRef + '\'' +
                        "}";
    }
}