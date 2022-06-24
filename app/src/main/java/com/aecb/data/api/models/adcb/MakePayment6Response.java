package com.aecb.data.api.models.adcb;

import com.aecb.data.api.models.BaseResponse;
import com.google.gson.annotations.SerializedName;

public class MakePayment6Response extends BaseResponse {

    @SerializedName("data")
    private MakePayment6Data data;

    public void setData(MakePayment6Data data) {
        this.data = data;
    }

    public MakePayment6Data getData() {
        return data;
    }
}