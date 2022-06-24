package com.aecb.data.api.models.payment_method;

public class AddCardResponseItems {
    private String paymentRef;
    private String resultCode;
    private String resultMessage;
    private String paymentdate;
    private String transactionID;
    private String paymentSource;

    public AddCardResponseItems(String paymentRef, String resultCode, String resultMessage, String paymentdate,
                                String transactionID, String paymentSource) {
        this.paymentRef = paymentRef;
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.paymentdate = paymentdate;
        this.transactionID = transactionID;
        this.paymentSource = paymentSource;
    }

    public String getPaymentSource() {
        return paymentSource;
    }

    public void setPaymentSource(String paymentSource) {
        this.paymentSource = paymentSource;
    }

    public String getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
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

    public String getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(String paymentdate) {
        this.paymentdate = paymentdate;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
}