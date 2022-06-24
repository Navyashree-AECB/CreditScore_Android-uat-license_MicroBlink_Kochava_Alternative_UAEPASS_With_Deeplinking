package com.aecb.data.api.models.edirham;

import com.google.gson.annotations.SerializedName;

public class PurchaseCheckoutResponse {

    @SerializedName("errorMessage")
    private String errorMessage;

    @SerializedName("checkoutId")
    private String checkoutId;

    @SerializedName("responseCode")
    private int responseCode;

    @SerializedName("checkoutUrl")
    private String checkoutUrl;

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getCheckoutId() {
        return checkoutId;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }
}