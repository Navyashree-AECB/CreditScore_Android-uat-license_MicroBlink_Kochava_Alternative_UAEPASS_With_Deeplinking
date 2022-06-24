package com.aecb.data.api.models.edirham;

import com.google.gson.annotations.SerializedName;

public class CheckoutStatusRequest {

    @SerializedName("password")
    private String password;

    @SerializedName("orderNumber")
    private String orderNumber;

    @SerializedName("userName")
    private String userName;

    @SerializedName("checkoutId")
    private String checkoutId;

    public String getPassword() {
        return password;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getCheckoutId() {
        return checkoutId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }
}