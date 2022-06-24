package com.aecb.data.api.models.verifyotp;

import com.aecb.data.api.models.BaseResponse;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class VerifyOtpResponse extends BaseResponse {

    @SerializedName("data")
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}