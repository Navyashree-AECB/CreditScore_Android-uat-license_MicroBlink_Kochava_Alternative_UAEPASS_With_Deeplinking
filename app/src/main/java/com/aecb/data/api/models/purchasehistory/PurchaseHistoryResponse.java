package com.aecb.data.api.models.purchasehistory;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class PurchaseHistoryResponse {

    @SerializedName("data")
    private PurchaseHistoryData data;

    public PurchaseHistoryData getData() {
        return data;
    }

    public void setData(PurchaseHistoryData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return
                "PurchaseHistoryResponse{" +
                        "data = '" + data + '\'' +
                        "}";
    }
}